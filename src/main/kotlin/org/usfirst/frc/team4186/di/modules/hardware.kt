package org.usfirst.frc.team4186.di.modules

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.Talon


object Hardware {
    const val MOTOR = "motor-test"

    const val IR = "ir-test"

  const val TIMESTAMP = "timestamp"
}

object PWMMap {
  object Motor {
    const val TEST_MOTOR = 0
  }
}

object DIOMap {
  object Sensor {
    const val IR = 0
  }
}

val hardware_module = Kodein.Module {
  bind<AHRS>() with instance(AHRS(SPI.Port.kMXP))

  bind<DigitalInput>(tag = Hardware.IR) with instance(DigitalInput(DIOMap.Sensor.IR))

  bind<SpeedController>(tag = Hardware.MOTOR) with singleton {
    Talon(PWMMap.Motor.TEST_MOTOR)
  }
}

