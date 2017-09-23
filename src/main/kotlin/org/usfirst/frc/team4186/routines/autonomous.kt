package org.usfirst.frc.team4186.routines

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.Scheduler
import org.usfirst.frc.team4186.extensions.execute
import org.usfirst.frc.team4186.robot.State


class Autonomous(
    private val scheduler: Scheduler,
    private val turn: (angle: Double) -> Command,
    private val move: (distance: Double) -> Command
) : State {
  private val commands by lazy {

    execute("AutonomousSequence") {
      -move(264.5)
      -turn(60.0)
      -move(142.25)
 //      -(move(142.25) timeoutIn 200.0)
    }
  }

  override fun init() {
    commands.start()
  }

  override fun tick() = scheduler.run()

  override fun cleanUp() {
    commands.cancel()
  }
}


