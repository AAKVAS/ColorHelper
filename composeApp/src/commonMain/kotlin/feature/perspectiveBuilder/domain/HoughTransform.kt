package feature.perspectiveBuilder.domain

import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt


class HoughTransform(
    private val thetaSteps: Int = 180,
    private val threshold: Int = 20,
    private val localMaxWindow: Int = 3
) {
    private class LineVote(
        var votes: Int = 0,
        var minProj: Float = Float.MAX_VALUE,
        var maxProj: Float = -Float.MAX_VALUE
    )

    fun detectLines(edges: BlackWhiteImage): List<Line> {
        val width = edges.width
        val height = edges.height

        val maxRho = sqrt((width * width + height * height).toDouble()).toInt()

        val accumulator = Array(maxRho * 2) {
            Array(thetaSteps) { LineVote() }
        }

        vote(edges, accumulator, maxRho)

        return findLines(accumulator, maxRho)
    }

    private fun vote(edges: BlackWhiteImage, accumulator: Array<Array<LineVote>>, maxRho: Int) {
        val width = edges.width
        val height = edges.height

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (edges.image[y * width + x]) {
                    for (thetaIdx in 0 until thetaSteps) {
                        val theta = Math.toRadians(thetaIdx.toDouble())
                        val rho = x * cos(theta) + y * sin(theta)

                        val rhoIdx = (rho + maxRho).toInt()
                        if (rhoIdx in 0 until maxRho * 2) {

                            val alongX = -sin(theta)
                            val alongY = cos(theta)
                            val projection = (x * alongX + y * alongY).toFloat()

                            val cell = accumulator[rhoIdx][thetaIdx]
                            cell.votes++

                            cell.minProj = min(cell.minProj, projection)
                            cell.maxProj = max(cell.maxProj, projection)
                        }
                    }
                }
            }
        }
    }

    private fun findLines(accumulator: Array<Array<LineVote>>, maxRho: Int): List<Line> {
        val lines = mutableListOf<Line>()

        for (rhoIdx in 0 until maxRho * 2) {
            for (thetaIdx in 0 until thetaSteps) {
                val cell = accumulator[rhoIdx][thetaIdx]
                val votes = cell.votes

                if (votes > threshold && isLocalMaximum(accumulator, rhoIdx, thetaIdx)) {
                    val rho = rhoIdx - maxRho
                    val theta = Math.toRadians(thetaIdx.toDouble())

                    val length = if (cell.maxProj > cell.minProj) {
                        (cell.maxProj - cell.minProj)
                    } else {
                        0f
                    }
                    val density = if (length > 0) votes / length else 0f

                    lines.add(
                        Line(
                            rho = rho.toDouble(),
                            theta = theta,
                            votes = votes,
                            length = length,
                            density = density
                        )
                    )
                }
            }
        }

        return lines
    }

    private fun isLocalMaximum(
        accumulator: Array<Array<LineVote>>,
        rhoIdx: Int,
        thetaIdx: Int
    ): Boolean {
        val votes = accumulator[rhoIdx][thetaIdx].votes
        val offset = localMaxWindow / 2

        for (dr in -offset..offset) {
            for (dt in -offset..offset) {
                if (dr == 0 && dt == 0) continue

                val nr = rhoIdx + dr
                val nt = thetaIdx + dt

                if (nr in accumulator.indices && nt in 0 until thetaSteps) {
                    if (accumulator[nr][nt].votes >= votes) {
                        return false
                    }
                }
            }
        }

        return true
    }
}