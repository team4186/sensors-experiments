package org.usfirst.frc.team4186.extensions

import edu.wpi.first.wpilibj.MotorSafety


interface MotorSafetyAdapter : MotorSafety {
  fun feed()
}

// TODO ditch motor safety helper
class MotorSafetyAdapterImpl(
    private val description: String,
    private var enabled: Boolean,
    private var expiration: Double = MotorSafety.DEFAULT_SAFETY_EXPIRATION,
    private val timestampProvider: () -> Double
) : MotorSafetyAdapter {

  private var stopTime = timestampProvider()

  override fun setExpiration(timeout: Double) {
    expiration = timeout
  }

  override fun getExpiration() = expiration

  override fun setSafetyEnabled(enabled: Boolean) {
    this.enabled = enabled
  }

  override fun isSafetyEnabled() = enabled

  override fun getDescription() = description

  // NOTE override on client
  override fun stopMotor() {}

  override fun isAlive() = !enabled || stopTime < timestampProvider()

  override fun feed() {
    stopTime = timestampProvider() + expiration
  }
}