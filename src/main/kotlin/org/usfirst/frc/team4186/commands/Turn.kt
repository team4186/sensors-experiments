package org.usfirst.frc.team4186.commands

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.command.Command
import org.usfirst.frc.team4186.drive.TankDrive

class Turn(
    private val ahrs: AHRS,
    private val drive: TankDrive,
    private val angle: Double = 0.0
) : Command("Turn $angle") {
  private val pid = PIDController(
      0.03,
      0.0,
      0.0,
      0.0,
      ahrs,
      { value -> drive.drive(value, -value) }
  ).apply {
    setInputRange(-180.0, 180.0)
    setAbsoluteTolerance(2.0)
    setOutputRange(-0.5, 0.5)
    setContinuous(true)
    disable()
  }

  override fun initialize() {
    ahrs.reset()
    pid.setpoint = angle
    pid.enable()
  }

  override fun end() {
    pid.disable()
    drive.stopMotor()
  }

  override fun interrupted() = end()

  override fun isFinished(): Boolean = pid.onTarget()

}