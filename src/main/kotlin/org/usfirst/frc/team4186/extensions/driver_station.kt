package org.usfirst.frc.team4186.extensions

import edu.wpi.first.wpilibj.DriverStation

object DSState {
  const val kStateDisabled = 0x0
  const val kStateEnabled = 0x1
  const val kStateTest = 0x3
  const val kStateAutonomous = 0x5
  const val kStateTeleop = 0x9
}

val DriverStation.state get() = when {
  isDisabled -> DSState.kStateDisabled
  isTest -> DSState.kStateTest
  isAutonomous -> DSState.kStateAutonomous
  isOperatorControl -> DSState.kStateTeleop
  isEnabled -> DSState.kStateEnabled
  else -> TODO("Error?")
}