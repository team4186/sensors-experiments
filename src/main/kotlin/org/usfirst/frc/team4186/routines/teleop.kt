package org.usfirst.frc.team4186.routines

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.Counter
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team4186.robot.State

class Teleop(
    private val motor: SpeedController,
    private val ir: DigitalInput,
    private val timestamp: () -> Double,
    private val ahrs: AHRS
) : State {

  val counter = Counter(ir)
  val joystick = Joystick(0)

  var start = timestamp()
  var lastCount = 0

  override fun init() {
    println("Teleop Init")
    start = timestamp()
  }

  override fun tick() {
    val now = timestamp()
    val count = counter.get()
    val axis = joystick.getRawAxis(0)
    motor.set(1.0 * axis)

    if ((now - start) > 1) {
      SmartDashboard.putNumber("ticksps", (count - lastCount).toDouble())
      start = now
      lastCount = count
    }

    if (joystick.getRawButton(1)) {
      counter.reset()
      start = now
      lastCount = 0
      SmartDashboard.putNumber("ticksps", 0.0)
    }

    SmartDashboard.putBoolean("ir", ir.get())
    SmartDashboard.putNumber("Input", axis)
    SmartDashboard.putNumber("ticks", count.toDouble())
//    SmartDashboard.putNumber("ir-rise", ir.readRisingTimestamp())
//    SmartDashboard.putNumber("ir-fall", ir.readFallingTimestamp())
    SmartDashboard.putNumber("heading", ahrs.fusedHeading.toDouble())
  }

  override fun cleanUp() {
    println("Teleop clean up")
    motor.stopMotor()
  }
}
