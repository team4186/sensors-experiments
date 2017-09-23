package org.usfirst.frc.team4186.drive

import edu.wpi.first.wpilibj.MotorSafety
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances.kRobotDrive_ArcadeStandard
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType.kResourceType_RobotDrive
import org.usfirst.frc.team4186.di.modules.HalReporter
import org.usfirst.frc.team4186.extensions.*

class TwinMotorArcadeDrive(
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
    halReporter(kResourceType_RobotDrive, 2, kRobotDrive_ArcadeStandard, "")
  }

  fun drive(move: Double, rotate: Double) {
    subMax(
        move.clamp().square(),
        rotate.clamp().square()
    ) { sub, max ->
      val movSignal = move.signal
      val sigSub = sub * movSignal
      val sigMax = max * movSignal

      if (movSignal == rotate.signal) {
        setMotorOutput(sigSub, sigMax)
      } else {
        setMotorOutput(sigMax, sigSub)
      }
    }
  }

  override fun stopMotor() {
    leftMotor.stopMotor()
    rightMotor.stopMotor()
  }

  private fun setMotorOutput(left: Double, right: Double) {
    leftMotor.set(left.clamp())
    rightMotor.set(-right.clamp())
    safety.feed()
  }
}

