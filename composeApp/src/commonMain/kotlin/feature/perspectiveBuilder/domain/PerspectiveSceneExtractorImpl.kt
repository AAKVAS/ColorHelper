package feature.perspectiveBuilder.domain

import core.model.Image
import feature.perspectiveBuilder.model.PerspectivePoint
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class PerspectiveSceneExtractorImpl : PerspectiveSceneExtractor {
    private val houghTransform = HoughTransform(
        thetaSteps = 180,
        threshold = 50,
        localMaxWindow = 3
    )

    override suspend fun extractPerspectiveScene(image: Image): List<PerspectivePoint> {
        val grayImage = convertToGrayscale(image)

        val edges = detectEdges(grayImage)

        val lines = houghTransform.detectLines(edges)

        return findVanishingPoints(lines, image.height, image.width)
    }

    private fun convertToGrayscale(image: Image): GrayImage {
        // Rec. 709
        val gray = IntArray(image.width * image.height) { i ->
            val pixel = image.pixels[i]
            (0.2126 * pixel.r + 0.7152 * pixel.g + 0.0722 * pixel.b).toInt()
        }

        return GrayImage(
            image = gray,
            image.height,
            image.width
        )
    }

    private fun detectEdges(grayImage: GrayImage): BlackWhiteImage {
        val edges = BooleanArray(grayImage.height * grayImage.width) { false }
        val thresholdSq = 16384

        for (y in 1 until grayImage.height - 1) {
            for (x in 1 until grayImage.width - 1) {
                val gx = computeGradientX(grayImage, x, y)
                val gy = computeGradientY(grayImage, x, y)

                if (gx * gx + gy * gy > thresholdSq) {
                    edges[y * grayImage.width + x] = true
                }
            }
        }

        return BlackWhiteImage(edges, grayImage.height, grayImage.width)
    }

    private fun computeGradientX(grayImage: GrayImage, x: Int, y: Int): Int {
        with(grayImage) {
            val idx = y * grayImage.width + x

            return (
                -image[idx - width - 1]
                + image[idx - width + 1]
                -2 * image[idx - 1]
                + 2 * image[idx + 1]
                -image[idx + width - 1]
                + image[idx + width + 1]
            )
        }
    }

    private fun computeGradientY(grayImage: GrayImage, x: Int, y: Int): Int {
        with(grayImage) {
            val idx = y * width + x

            return (
                -image[idx - width - 1]
                - 2 * image[idx - width]
                - image[idx - width + 1]
                + image[idx + width - 1]
                + 2 * image[idx + width]
                + image[idx + width + 1]
            )
        }
    }

    private fun findVanishingPoints(
        lines: List<Line>,
        height: Int,
        width: Int,
    ): List<PerspectivePoint> {
        if (lines.size < 2) return emptyList()

        val strongLines = lines.take(30)

        val parallelGroups = findParallelLines(lines)
        val infinitePoints = parallelGroups.map { group ->
            val avgAngle = calculateCircularAverage(group.angles)
            PerspectivePoint.infinite(avgAngle)
        }

        val finitePoints = findIntersectionPoints(strongLines, height, width)
            .filter { point ->
                !isPointNearInfiniteDirection(point, infinitePoints, height, width,)
            }

        return (infinitePoints.take(2) + finitePoints).take(4)
    }

    private data class ParallelGroup(
        val lines: List<Line>,
        val angles: List<Float>
    )

    private fun findParallelLines(lines: List<Line>): List<ParallelGroup> {
        val denseLines = lines.filter { (it.density ?: 0f) >= 0.7f }
        val angles = denseLines.map {
            (Math.toDegrees(it.theta).toFloat() + 90) % 180 to (it.density ?: 0f)
        }

        val clusters = clusterAngles(angles, step = 10).filter { it.size >= 3 }

        val maxClusterDensity = clusters.maxOfOrNull { it.avgDensity } ?: 1.0f

        val filteredClusters = clusters
            .filter {
                it.avgDensity >= maxClusterDensity * 0.93f
            }
            .sortedByDescending { it.avgDensity }

        val uniqueClusters = mutableListOf<AngleCluster>()
        val angleThreshold = 15f

        for (cluster in filteredClusters) {
            val tooClose = uniqueClusters.any { existing ->
                val diff = abs(existing.center - cluster.center)
                minOf(diff, 180 - diff) < angleThreshold
            }
            if (!tooClose) {
                uniqueClusters.add(cluster)
            }
        }

         return uniqueClusters.map { cluster ->
            val clusterLines = denseLines.filterIndexed { index, _ ->
                angles[index].first in cluster.angles
            }
            ParallelGroup(clusterLines, cluster.angles)
        }
    }

    private data class AngleCluster(
        val center: Float,
        val angles: List<Float>,
        val size: Int,
        val avgDensity: Float
    )

    private fun clusterAngles(angles: List<Pair<Float, Float>>, step: Int): List<AngleCluster> {
        if (angles.isEmpty()) return emptyList()
        val bins = mutableMapOf<Int, MutableList<Pair<Float, Float>>>()
        val halfStep = step / 2

        for (angle in angles) {
            val shifted = (angle.first + halfStep) % 180
            val binIndex = (shifted / step).toInt()
            bins.getOrPut(binIndex) { mutableListOf() }.add(angle)
        }

        return bins.map { (_, binAngles) ->
            val sumSin = binAngles.sumOf {
                Math.toRadians((it.first * 2).toDouble()).let { rad -> sin(rad) }
            }
            val sumCos = binAngles.sumOf {
                Math.toRadians((it.first * 2).toDouble()).let { rad -> cos(rad) }
            }

            val center = (Math.toDegrees(atan2(sumSin, sumCos)).toFloat() / 2 + 180) % 180
            val avgDensity = binAngles.map { it.second }.average().toFloat()
            AngleCluster(center, binAngles.map { it.first }, binAngles.size, avgDensity)
        }
    }

    private fun findIntersectionPoints(
        lines: List<Line>,
        height: Int,
        width: Int,
    ): List<PerspectivePoint> {
        if (lines.size < 2) return emptyList()

        val intersections = mutableListOf<Pair<Float, Float>>()

        for (i in 0 until lines.size - 1) {
            for (j in i + 1 until lines.size) {
                val intersection = findIntersection(lines[i], lines[j])
                if (intersection != null) {
                    intersections.add(intersection)
                }
            }
        }

        if (intersections.isEmpty()) return emptyList()

        val diagonalSq = width * width + height * height
        val thresholdSq  = diagonalSq  * 0.0004f
        val clusters = clusterIntersections(intersections, thresholdSq)
        val limit = max(width, height) * 5f

        return clusters
            .filter { cluster ->
                cluster.points.size >= 3 &&
                abs(cluster.centerX) < limit &&
                abs(cluster.centerY) < limit
            }
            .sortedByDescending { cluster -> cluster.points.size }
            .map { cluster -> PerspectivePoint(cluster.centerX, cluster.centerY) }
    }

    private fun findIntersection(line1: Line, line2: Line): Pair<Float, Float>? {
        val theta1 = line1.theta
        val theta2 = line2.theta
        val rho1 = line1.rho
        val rho2 = line2.rho

        val a1 = cos(theta1)
        val b1 = sin(theta1)
        val a2 = cos(theta2)
        val b2 = sin(theta2)

        val determinant = a1 * b2 - a2 * b1

        if (abs(determinant) < 1e-6) return null

        val x = (b2 * rho1 - b1 * rho2) / determinant
        val y = (a1 * rho2 - a2 * rho1) / determinant

        return x.toFloat() to y.toFloat()
    }

    private fun clusterIntersections(
        points: List<Pair<Float, Float>>,
        thresholdSq: Float
    ): List<Cluster> {
        val clusters = mutableListOf<Cluster>()

        for (point in points) {
            var foundCluster = false

            for (cluster in clusters) {
                val distance = (point.first - cluster.centerX).pow(2) +
                        (point.second - cluster.centerY).pow(2)

                if (distance < thresholdSq) {
                    cluster.addPoint(point)
                    foundCluster = true
                    break
                }
            }

            if (!foundCluster) {
                clusters.add(Cluster().apply { addPoint(point) })
            }
        }

        return clusters
    }

    private class Cluster {
        val points = mutableListOf<Pair<Float, Float>>()
        var centerX = 0f
        var centerY = 0f

        fun addPoint(point: Pair<Float, Float>) {
            val newSize = points.size + 1
            centerX = (centerX * points.size + point.first) / newSize
            centerY = (centerY * points.size + point.second) / newSize
            points.add(point)
        }
    }

    private fun isPointNearInfiniteDirection(
        point: PerspectivePoint,
        infinitePoints: List<PerspectivePoint>,
        height: Int,
        width: Int,
    ): Boolean {
        val centerX = width / 2f
        val centerY = height / 2f

        val dx = point.x - centerX
        val dy = point.y - centerY
        val pointAngle = atan2(dy, dx)
        val angleLimit = 10f
        val distanceLimit = max(width, height) * 2

        for (infinitePoint in infinitePoints) {
            if (infinitePoint.direction != null) {
                val angleDiff = abs(pointAngle - infinitePoint.direction) % 180
                val minDiff = minOf(angleDiff, 180 - angleDiff)

                if (minDiff < angleLimit) {
                    val distance = sqrt(dx * dx + dy * dy)
                    if (distance > distanceLimit) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun calculateCircularAverage(angles: List<Float>): Float {
        if (angles.isEmpty()) return 0f

        var sumSin = 0.0
        var sumCos = 0.0

        for (angle in angles) {
            val rad = Math.toRadians((angle * 2).toDouble())
            sumSin += sin(rad)
            sumCos += cos(rad)
        }

        val avgRad = atan2(sumSin, sumCos)
        return ((Math.toDegrees(avgRad).toFloat() / 2) + 180) % 180
    }
}