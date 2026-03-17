package feature.perspectiveBuilder.domain

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class HoughTransform(
    private val thetaSteps: Int = 180,
    private val threshold: Int = 50,
    private val localMaxWindow: Int = 3
) {
    fun detectLines(edges: GrayImage): List<Line> {
        val width = edges.width
        val height = edges.height

        val maxRho = sqrt((width * width + height * height).toDouble()).toInt()

        val accumulator = Array(maxRho * 2) { IntArray(thetaSteps) }

        vote(edges, accumulator, maxRho)

        return findLines(accumulator, maxRho)
    }

    private fun vote(edges: GrayImage, accumulator: Array<IntArray>, maxRho: Int) {
        val width = edges.width
        val height = edges.height

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (edges.image[y * width + x] > 0) {
                    for (thetaIdx in 0 until thetaSteps) {
                        val theta = Math.toRadians(thetaIdx.toDouble())
                        val rho = x * cos(theta) + y * sin(theta)

                        val rhoIdx = (rho + maxRho).toInt()
                        if (rhoIdx in 0 until maxRho * 2) {
                            accumulator[rhoIdx][thetaIdx]++
                        }
                    }
                }
            }
        }
    }

    private fun findLines(accumulator: Array<IntArray>, maxRho: Int): List<Line> {
        val lines = mutableListOf<Line>()

        for (rhoIdx in 0 until maxRho * 2) {
            for (thetaIdx in 0 until thetaSteps) {
                val votes = accumulator[rhoIdx][thetaIdx]

                if (votes > threshold && isLocalMaximum(accumulator, rhoIdx, thetaIdx)) {
                    val rho = rhoIdx - maxRho
                    val theta = Math.toRadians(thetaIdx.toDouble())
                    lines.add(Line(rho.toDouble(), theta, votes))
                }
            }
        }

        return lines.sortedByDescending { it.votes }
    }

    private fun isLocalMaximum(
        accumulator: Array<IntArray>,
        rhoIdx: Int,
        thetaIdx: Int
    ): Boolean {
        val votes = accumulator[rhoIdx][thetaIdx]
        val offset = localMaxWindow / 2

        for (dr in -offset..offset) {
            for (dt in -offset..offset) {
                if (dr == 0 && dt == 0) continue

                val nr = rhoIdx + dr
                val nt = thetaIdx + dt

                if (nr in accumulator.indices && nt in 0 until thetaSteps) {
                    if (accumulator[nr][nt] >= votes) {
                        return false
                    }
                }
            }
        }

        return true
    }
}