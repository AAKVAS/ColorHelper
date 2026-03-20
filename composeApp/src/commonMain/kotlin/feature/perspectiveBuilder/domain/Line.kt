package feature.perspectiveBuilder.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
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

    fun toPoints(width: Int, height: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val a = cos(theta)
        val b = sin(theta)
        val x0 = a * rho
        val y0 = b * rho

        val length = max(width, height) * 2

        val x1 = (x0 + length * (-b)).toInt()
        val y1 = (y0 + length * (a)).toInt()
        val x2 = (x0 - length * (-b)).toInt()
        val y2 = (y0 - length * (a)).toInt()

        return (x1 to y1) to (x2 to y2)
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