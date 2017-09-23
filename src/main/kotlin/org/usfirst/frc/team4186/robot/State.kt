package org.usfirst.frc.team4186.robot

interface State {
  fun init()
  fun tick()
  fun cleanUp()
}