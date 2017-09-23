package org.usfirst.frc.team4186.extensions

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup


inline fun execute(
    name: String,
    block: CommandBuilder.() -> Unit
) = CommandBuilder(name).apply {
  block()
}.target

class CommandBuilder(name: String) {
  val target = CommandGroup(name)
  operator fun Command.unaryPlus() = target.addParallel(this)
  operator fun Command.unaryMinus() = target.addSequential(this)
  operator fun Pair<Command, Double>.unaryPlus() = target.addParallel(first, second)
  operator fun Pair<Command, Double>.unaryMinus() = target.addSequential(first, second)

  infix fun Command.timeoutIn(timeout: Double) = Pair(this, timeout)
}
