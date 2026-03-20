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
        threshold = 20,
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

        val parallelGroups = findParallelLines(lines)
        val infinitePoints = parallelGroups.map { group ->
            val angles = group.map {it.angle }
            val avgAngle = calculateCircularCenter(angles)
            PerspectivePoint.infinite(avgAngle)
        }

        val strongLines = takeStrongLines(lines, parallelGroups)
        val rawFinite  = findIntersectionPoints(strongLines, height, width)
            .filter { point ->
                !isPointNearInfiniteDirection(point, infinitePoints, height, width,)
            }

        val finitePoints = sortVanishingPoints(rawFinite, width, height)

        return infinitePoints.take(3) + finitePoints.take(3)
    }

    private fun takeStrongLines(lines: List<Line>, parallelGroups: List<List<Line>>): List<Line> {
        val parallelAngles = parallelGroups.flatMap { lines ->
            lines.map {
                it.angle
            }
        }.distinct()
        val tolerance = 7f

        val sortedVotes = lines.map {it.votes}.sortedDescending()
        val threshold = sortedVotes[sortedVotes.size * 35 / 100]

        return lines.filter { line ->
            line.votes >= threshold && line.density >= 0.9f && parallelAngles.none { parallelAngle ->
                val diff = abs(line.angle - parallelAngle)
                minOf(diff, 180 - diff) < tolerance
            }
        }
        .sortedWith(compareByDescending<Line> { it.density }.thenByDescending { it.votes })
        .take(40)
    }


    private fun findParallelLines(lines: List<Line>): List<List<Line>> {
        val denseLines = lines.filter { it.density >= 0.8f }

        val clusters = clusterLinesByAngles(denseLines, step = 10).filter { it.size >= 3 }

        val maxClusterDensity = clusters.maxOfOrNull { it.avgDensity } ?: 1.0f
        val maxVotes = clusters.maxOfOrNull { it.topVotes } ?: 1

        val filteredClusters = clusters
            .filter {
                it.avgDensity >= maxClusterDensity * 0.93f
                && it.topVotes >= maxVotes * 0.8
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

         return uniqueClusters.map { it.lines }
    }

    private data class AngleCluster(
        val center: Float,
        val lines: List<Line>,
        val size: Int,
        val avgDensity: Float,
        val topVotes: Int
    )

    private fun clusterLinesByAngles(lines: List<Line>, step: Int): List<AngleCluster> {
        if (lines.isEmpty()) return emptyList()
        val bins = mutableMapOf<Int, MutableList<Line>>()
        val halfStep = step / 2

        for (line in lines) {
            val shifted = (line.angle + halfStep) % 180
            val binIndex = (shifted / step).toInt()
            bins.getOrPut(binIndex) { mutableListOf() }.add(line)
        }

        return bins.map { (_, binAngles) ->
            val center = calculateCircularCenter(binAngles.map { it.angle })
            val avgDensity = binAngles.map { it.density }.average().toFloat()
            val sortedVotes = binAngles.map {it.votes}.sortedDescending()
            val topVotes = sortedVotes[sortedVotes.size * 20 / 100]
            AngleCluster(center, binAngles, binAngles.size, avgDensity, topVotes)
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

    private fun sortVanishingPoints(points: List<PerspectivePoint>, width: Int, height: Int): List<PerspectivePoint> {
        if (points.isEmpty()) return emptyList()

        val threshold = max(width, height) * 0.01f
        val uniqueResult = mutableListOf<PerspectivePoint>()
        val similarPoints = mutableListOf<PerspectivePoint>()

        for (point in points) {
            val uniquePointExists = uniqueResult.any { existing ->
                val distance =
                    (point.x - existing.x).pow(2) + (point.y - existing.y).pow(2)
                distance < threshold
            }

            val similarPointExists = similarPoints.any { existing ->
                val distance =
                    (point.x - existing.x).pow(2) + (point.y - existing.y).pow(2)
                distance < threshold
            }

            if (uniquePointExists || similarPointExists) {
                similarPoints.add(point)
            } else {
                uniqueResult.add(point)
            }
        }
        return uniqueResult + similarPoints
    }
}