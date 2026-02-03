package feature.palette.photoPicker.paletteExtractor

import androidx.compose.ui.graphics.Color

class ColorThiefPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        val quantized = pixels.map { pixel ->
            RGBPixel(
                r = (pixel.r / 32) * 32,
                g = (pixel.g / 32) * 32,
                b = (pixel.b / 32) * 32
            )
        }


        val histogram = mutableMapOf<RGBPixel, Int>()
        quantized.forEach { pixel ->
            histogram[pixel] = histogram.getOrDefault(pixel, 0) + 1
        }

        val topColors = histogram.entries
            .sortedByDescending { it.value }
            .take(k * 3)

        val resultColors = mutableListOf<Color>()

        for ((colorPixel, _) in topColors) {
            val color = colorPixel.toColor()

            val isTooSimilar = resultColors.any { existingColor ->
                colorDistance(color, existingColor) < 50.0
            }

            if (!isTooSimilar) {
                resultColors.add(color)
                if (resultColors.size >= k) break
            }
        }

        if (resultColors.size < k) {
            val remaining = topColors
                .map { it.key.toColor() }
                .filter { it !in resultColors }
                .take(k - resultColors.size)

            resultColors.addAll(remaining)
        }

        return resultColors
    }

    private fun colorDistance(c1: Color, c2: Color): Double {
        val r1 = c1.red * 255
        val g1 = c1.green * 255
        val b1 = c1.blue * 255

        val r2 = c2.red * 255
        val g2 = c2.green * 255
        val b2 = c2.blue * 255

        return kotlin.math.sqrt(
            (r1 - r2) * (r1 - r2) +
                    (g1 - g2) * (g1 - g2) +
                    (b1 - b2) * (b1 - b2)
        ).toDouble()
    }
}