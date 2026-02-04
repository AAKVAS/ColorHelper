package feature.palette.photoPicker.domain

import androidx.compose.ui.graphics.Color
import feature.palette.photoPicker.model.RGBPixel

class ColorThiefPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        val histogram = mutableMapOf<RGBPixel, Int>()
        pixels.forEach { pixel ->
            val quantized = RGBPixel(
                r = (pixel.r / 32) * 32,
                g = (pixel.g / 32) * 32,
                b = (pixel.b / 32) * 32
            )
            histogram[quantized ] = histogram.getOrDefault(quantized, 0) + 1
        }

        val topColors = histogram.entries
            .sortedByDescending { it.value }
            .take(k * 3)

        val resultColors = mutableListOf<Color>()

        for ((colorPixel, _) in topColors) {
            val color = colorPixel.toColor()

            val isTooSimilar = resultColors.any { existingColor ->
                colorDistanceSquared(color, existingColor) < 2500.0
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
}