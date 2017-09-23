package org.usfirst.frc.team4186.commands

import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Command

class PowerMotor(
    private val motor: SpeedController,
    private val output: Double = 0.0
) : Command("PowerMotor $output") {

  override fun initialize() {

  }

  override fun execute() {
    motor.set(output)
  }

  override fun end() {
    motor.stopMotor()
  }

  override fun isFinished(): Boolean = false
}