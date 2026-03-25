package feature.perspectiveBuilder.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class Line(
    val rho: Double,
    val theta: Double,
    val votes: Int = 0,
    val length: Float? = null,
    val density: Float
) {
    val angle: Float by lazy {
        (Math.toDegrees(theta).toFloat() + 90) % 180
    }
}

fun calculateCircularCenter(angles: List<Float>): Float {
    val sumSin = angles.sumOf {
        sin(Math.toRadians((it * 2).toDouble()))
    }
    val sumCos = angles.sumOf {
        cos(Math.toRadians((it * 2).toDouble()))
    }
    return ((Math.toDegrees(atan2(sumSin, sumCos)).toFloat() / 2) + 180) % 180
}