package org.usfirst.frc.team4186.robot

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances.kFramework_Iterative
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType.kResourceType_Framework
import edu.wpi.first.wpilibj.hal.HAL
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.usfirst.frc.team4186.di.modules.*
import org.usfirst.frc.team4186.extensions.DSState
import org.usfirst.frc.team4186.extensions.state
import org.usfirst.frc.team4186.routines.init
import org.usfirst.frc.team4186.routines.util.nop

class Robot : RobotBase(), LazyKodeinAware {

  override val kodein = Kodein.lazy {
    import(services_module)
    import(hardware_module)
    import(states_module)
  }

  private val driverStation by instance<DriverStation>()
  private val halReporter by instance<HalReporter>()

  override fun startCompetition() {
    halReporter(kResourceType_Framework, kFramework_Iterative, 0, "")

    init()

    HAL.observeUserProgramStarting()
    LiveWindow.setEnabled(true)

    println("Staring Competition")
    while (true) {
      println("Main Loop")
      runBlocking(CommonPool) {
        loop(driverStation.state)
      }
    }
  }

  private suspend fun loop(stateCode: Int) {
    println("Loop state:$stateCode")
    LiveWindow.setEnabled(false)

    // NOTE Silly syntax, but here we need to get the proper value from the lazy accessor
    val state = instance<State>(stateCode).value
    state.init()
    val stateReport = stateCode.sendObserveUser
    while (driverStation.state == stateCode) {
      sync()
      stateReport()
      state.tick()
    }
    state.cleanUp()
  }

  private suspend fun sync() {
    async(CommonPool) { driverStation.waitForData(0.33) }.await()
  }

  private val Int.sendObserveUser: () -> Unit
    get() = when (this) {
      DSState.kStateDisabled -> HAL::observeUserProgramDisabled
      DSState.kStateEnabled -> ::nop
      DSState.kStateTest -> HAL::observeUserProgramTest
      DSState.kStateAutonomous -> HAL::observeUserProgramAutonomous
      DSState.kStateTeleop -> HAL::observeUserProgramTeleop
      else -> TODO("Error?")
    }

  private val Int.needLiveWindow
    get() = when (this) {
      DSState.kStateTest, DSState.kStateTeleop -> true
      else -> false
    }
}


