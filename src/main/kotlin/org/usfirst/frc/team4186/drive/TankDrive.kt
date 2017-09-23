package org.usfirst.frc.team4186.drive

import edu.wpi.first.wpilibj.MotorSafety
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances.kRobotDrive_Tank
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType.kResourceType_RobotDrive
import org.usfirst.frc.team4186.di.modules.HalReporter
import org.usfirst.frc.team4186.extensions.MotorSafetyAdapter
import org.usfirst.frc.team4186.extensions.square

class TankDrive(
    private val leftMotor: SpeedController,
    private val rightMotor: SpeedController,
    private val safety: MotorSafetyAdapter,
    private val halReporter: HalReporter
) : MotorSafety by safety {

  init {
    expiration = 0.1
    isSafetyEnabled = true
  }

  fun report() {
    halReporter(kResourceType_RobotDrive, 2, kRobotDrive_Tank, "")
  }

  fun drive(left: Double, right: Double) {
    leftMotor.set(Math.copySign(left.square(), left))
    rightMotor.set(-Math.copySign(right.square(), right))
    safety.feed()
  }

  override fun stopMotor() {
    leftMotor.stopMotor()
    rightMotor.stopMotor()
  }
}
