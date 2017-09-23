package org.usfirst.frc.team4186.extensions

inline fun subMax(a: Double, b: Double, block: (Double, Double) -> Unit) = block(a - b, maxOf(a, b))
val Double.signal: Double get() = Math.copySign(1.0, this)
fun Double.clamp(min: Double = -1.0, max: Double = 1.0) = maxOf(min, minOf(max, this))
fun Double.square() = this * this
