package org.usfirst.frc.team4186.di.modules

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.instance
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Command
import org.usfirst.frc.team4186.commands.PowerMotor
import org.usfirst.frc.team4186.commands.Turn
import org.usfirst.frc.team4186.drive.TankDrive

object Commands {
  const val RUN = "command-run"
  const val TURN = "command-turn"
}

val commands_module = Kodein.Module {
  bind<Command>(tag = Commands.RUN) with factory { output: Double ->
    PowerMotor(
        motor = instance<SpeedController>(Hardware.MOTOR),
        output = output
    )
  }

  bind<Command>(tag = Commands.TURN) with factory { angle: Double ->
    Turn(
        ahrs = instance<AHRS>(),
        drive = instance<TankDrive>(),
        angle = angle
    )
  }
}