package feature.palette.photoPicker.domain

import androidx.compose.ui.graphics.Color
import feature.palette.photoPicker.model.RGBPixel
import kotlin.random.Random


class KMeansPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        if (pixels.size <= k) {
            return pixels.distinctBy { it.toColor() }
                .take(k)
                .map { it.toColor() }
        }

        val centroids = kMeansPlusPlusInit(pixels, k)
        val assignments = IntArray(pixels.size)
        var changed: Boolean

        repeat(maxIterations) {
            changed = false
            val clusterSums = Array(k) { doubleArrayOf(0.0, 0.0, 0.0) }
            val clusterSizes = IntArray(k)

            pixels.forEachIndexed { index, pixel ->
                var minDistance = Double.MAX_VALUE
                var bestCluster = 0

                for (i in 0 until k) {
                    val distance = colorDistanceSquared(pixel, centroids[i]).toDouble()
                    if (distance < minDistance) {
                        minDistance = distance
                        bestCluster = i
                    }
                }

                if (assignments[index] != bestCluster) {
                    assignments[index] = bestCluster
                    changed = true
                }

                clusterSums[bestCluster][0] += pixel.r.toDouble()
                clusterSums[bestCluster][1] += pixel.g.toDouble()
                clusterSums[bestCluster][2] += pixel.b.toDouble()
                clusterSizes[bestCluster]++
            }

            if (!changed) return@repeat

            for (i in 0 until k) {
                if (clusterSizes[i] > 0) {
                    centroids[i] = RGBPixel(
                        r = (clusterSums[i][0] / clusterSizes[i]).toInt(),
                        g = (clusterSums[i][1] / clusterSizes[i]).toInt(),
                        b = (clusterSums[i][2] / clusterSizes[i]).toInt()
                    )
                } else {
                    centroids[i] = pixels[Random.nextInt(pixels.size)]
                }
            }
        }

        return centroids.map { it.toColor() }
    }

    private fun kMeansPlusPlusInit(pixels: List<RGBPixel>, k: Int): Array<RGBPixel> {
        val centroids = Array(k) { RGBPixel(0, 0, 0) }

        centroids[0] = pixels[Random.nextInt(pixels.size)]
        val distances = DoubleArray(pixels.size)

        for (i in 1 until k) {
            var totalDistance = 0.0

            for (j in pixels.indices) {
                var minDist = Double.MAX_VALUE
                for (c in 0 until i) {
                    val dist = colorDistanceSquared(pixels[j], centroids[c]).toDouble()
                    if (dist < minDist) {
                        minDist = dist
                    }
                }
                distances[j] = minDist
                totalDistance += distances[j]
            }

            val threshold = Random.nextDouble() * totalDistance
            var cumulative = 0.0
            var selectedIndex = 0

            for (j in pixels.indices) {
                cumulative += distances[j]
                if (cumulative >= threshold) {
                    selectedIndex = j
                    break
                }
            }

            centroids[i] = pixels[selectedIndex]
        }

        return centroids
    }
}