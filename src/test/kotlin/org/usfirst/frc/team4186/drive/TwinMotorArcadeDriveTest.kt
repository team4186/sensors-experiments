package org.usfirst.frc.team4186.drive

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import edu.wpi.first.wpilibj.SpeedController
import org.junit.Test
import org.usfirst.frc.team4186.extensions.MotorSafetyAdapter
import kotlin.test.assertTrue

class TwinMotorArcadeDriveTest {

  @Test
  fun drive() {
    arrayOf(
        0.0 to 0.0,
        1.0 to 0.0,
        0.5 to 0.0,
        -0.5 to 0.0,
        -1.0 to 0.0,
        0.0 to 1.0,
        0.0 to 0.5,
        0.0 to -0.5,
        0.0 to -1.0,
        1.0 to 1.0,
        0.5 to 0.5,
        -0.5 to 0.5,
        -1.0 to 1.0,
        1.0 to -1.0,
        0.5 to -0.5,
        -0.1 to -0.1,
        -0.5 to -0.5,
        -1.0 to -1.0
    ).let { input ->

      val leftOutput = argumentCaptor<Double>()
      val rightOutput = argumentCaptor<Double>()
      val altImpl = TwinMotorArcadeDrive(
          leftMotor = mock<SpeedController> { on { set(leftOutput.capture()) } doAnswer {} },
          rightMotor = mock<SpeedController> { on { set(rightOutput.capture()) } doAnswer {} },
          safety = mock<MotorSafetyAdapter> {},
          halReporter = { _, _, _, _ -> }
      )

      val frcOutput = Array<Pair<Double, Double>>(input.size){
        val (move, rotate) = input[it]
        FrcArcadeDrive.drive(move, rotate)
      }

      input.forEach { (move, rotate) -> altImpl.drive(move, rotate) }

      frcOutput
          .zip(leftOutput.allValues.zip(rightOutput.allValues))
          .forEachIndexed { i, (expected, actual) ->
            val (el, er) = expected
            val (al, ar) = actual
            println("input[${input[i]}]=(e=($el, $er), a=($al, $ar))")
            // NOTE using assertTrue instead of assertEquals because if compare Double (objects) equals fails
            // because Double(0.0) != Double(-0.0)
            assertTrue(el == al && er == ar)
          }
    }
  }
}

private object FrcArcadeDrive {
  fun drive(inMoveValue: Double, inRotateValue: Double, squaredInputs: Boolean = true): Pair<Double, Double> {
    val leftMotorSpeed: Double
    val rightMotorSpeed: Double

    var moveValue = inMoveValue.clamp()
    var rotateValue = inRotateValue.clamp()

    if (squaredInputs) {
      // square the inputs (while preserving the sign) to increase fine control
      // while permitting full power
      if (moveValue >= 0.0) {
        moveValue = moveValue * moveValue
      } else {
        moveValue = -(moveValue * moveValue)
      }
      if (rotateValue >= 0.0) {
        rotateValue = rotateValue * rotateValue
      } else {
        rotateValue = -(rotateValue * rotateValue)
      }
    }

    if (moveValue > 0.0) {
      if (rotateValue > 0.0) {
        leftMotorSpeed = moveValue - rotateValue
        rightMotorSpeed = maxOf(moveValue, rotateValue)
      } else {
        leftMotorSpeed = Math.max(moveValue, -rotateValue)
        rightMotorSpeed = moveValue + rotateValue
      }
    } else {
      if (rotateValue > 0.0) {
        leftMotorSpeed = -Math.max(-moveValue, rotateValue)
        rightMotorSpeed = moveValue + rotateValue
      } else {
        leftMotorSpeed = moveValue - rotateValue
        rightMotorSpeed = -Math.max(-moveValue, -rotateValue)
      }
    }

    // NOTE on original implementation rightMotorSpeed is negated!
    return leftMotorSpeed to -rightMotorSpeed
  }

  private fun Double.clamp(min: Double = -1.0, max: Double = 1.0) = maxOf(min, minOf(max, this))
}