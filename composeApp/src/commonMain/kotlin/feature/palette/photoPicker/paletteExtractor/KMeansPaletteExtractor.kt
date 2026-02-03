package feature.palette.photoPicker.paletteExtractor

import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt
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

        repeat(maxIterations) { iteration ->
            changed = false
            pixels.forEachIndexed { index, pixel ->
                var minDistance = Double.MAX_VALUE
                var bestCluster = assignments[index]

                centroids.forEachIndexed { clusterIndex, centroid ->
                    val distance = pixel.distanceTo(centroid)
                    if (distance < minDistance) {
                        minDistance = distance
                        bestCluster = clusterIndex
                    }
                }

                if (assignments[index] != bestCluster) {
                    assignments[index] = bestCluster
                    changed = true
                }
            }

            if (!changed) return@repeat

            val clusterSums = Array(k) { doubleArrayOf(0.0, 0.0, 0.0) }
            val clusterSizes = IntArray(k)

            pixels.forEachIndexed { index, pixel ->
                val cluster = assignments[index]
                clusterSums[cluster][0] += pixel.r.toDouble()
                clusterSums[cluster][1] += pixel.g.toDouble()
                clusterSums[cluster][2] += pixel.b.toDouble()
                clusterSizes[cluster]++
            }

            for (i in 0 until k) {
                if (clusterSizes[i] > 0) {
                    centroids[i] = RGBPixel(
                        r = (clusterSums[i][0] / clusterSizes[i]).toInt(),
                        g = (clusterSums[i][1] / clusterSizes[i]).toInt(),
                        b = (clusterSums[i][2] / clusterSizes[i]).toInt()
                    )
                }
            }
        }

        return centroids.map { it.toColor() }
    }

    private fun kMeansPlusPlusInit(pixels: List<RGBPixel>, k: Int): MutableList<RGBPixel> {
        val centroids = mutableListOf<RGBPixel>()

        centroids.add(pixels.random())

        for (i in 1 until k) {
            val distances = DoubleArray(pixels.size)
            var totalDistance = 0.0

            pixels.forEachIndexed { index, pixel ->
                var minDist = Double.MAX_VALUE
                centroids.forEach { centroid ->
                    val dist = pixel.distanceTo(centroid)
                    if (dist < minDist) {
                        minDist = dist
                    }
                }
                distances[index] = minDist * minDist
                totalDistance += distances[index]
            }

            val random = Random.nextDouble() * totalDistance
            var cumulative = 0.0
            var selectedIndex = 0

            for (j in distances.indices) {
                cumulative += distances[j]
                if (cumulative >= random) {
                    selectedIndex = j
                    break
                }
            }

            centroids.add(pixels[selectedIndex])
        }

        return centroids
    }

    private fun RGBPixel.distanceTo(other: RGBPixel): Double {
        val dr = (r - other.r).toDouble()
        val dg = (g - other.g).toDouble()
        val db = (b - other.b).toDouble()
        return sqrt(dr * dr + dg * dg + db * db)
    }

}