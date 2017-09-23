package org.usfirst.frc.team4186.di.modules

import com.github.salomonbrys.kodein.*
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.hal.HAL
import edu.wpi.first.wpilibj.networktables.NetworkTable
import org.usfirst.frc.team4186.drive.TankDrive
import org.usfirst.frc.team4186.drive.TwinMotorArcadeDrive
import org.usfirst.frc.team4186.extensions.MotorSafetyAdapterImpl
import org.usfirst.frc.team4186.hardware.SaitekX52Adapter

typealias HalReporter = (resource: Int, instanceNumber: Int, context: Int, feature: String) -> Unit

val services_module = Kodein.Module {
  bind<DriverStation>() with singleton { DriverStation.getInstance() }
  bind<NetworkTable>() with factory { table: String -> NetworkTable.getTable(table) }
  bind<HalReporter>() with instance({ r, i, c, f -> HAL.report(r, i, c, f) })
  bind<Double>(tag = Hardware.TIMESTAMP) with provider { Timer.getFPGATimestamp() }

  bind<Scheduler>() with singleton { Scheduler.getInstance() }

  bind<SaitekX52Adapter>() with singleton {
    SaitekX52Adapter(
        driverStation = instance(),
        port = 0,
        halReporter = instance()
    )
  }
}