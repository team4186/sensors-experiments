package org.usfirst.frc.team4186.commands

import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.Ultrasonic
import edu.wpi.first.wpilibj.command.Command
import org.usfirst.frc.team4186.drive.TankDrive

class Move (
    val sonar: Ultrasonic,
    private val drive: TankDrive,
    private val distance: Double = 0.0
) : Command("Move $distance") {

  private val pid = PIDController(
      0.5,
      0.004,
      0.0,
      0.0,
      sonar,
      { value -> drive.drive(value, value) }
  ).apply {
    setInputRange(0.0, 5.0)
    setAbsoluteTolerance(10.0)
    setOutputRange(-1.0, 1.0)
    setContinuous(false)
    disable()
  }

  override fun initialize() {
    pid.setpoint = distance
    pid.enable()

    val a:Int = distance.toInt()
  }

  override fun execute() {
    sonar.distanceUnits
  }

  override fun end() {
    pid.disable()
    drive.stopMotor()
  }

  override fun interrupted() = end()
  override fun isFinished(): Boolean = pid.onTarget()
}