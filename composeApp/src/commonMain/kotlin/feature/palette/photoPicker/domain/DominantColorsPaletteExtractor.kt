package feature.palette.photoPicker.domain

import androidx.compose.ui.graphics.Color
import feature.palette.photoPicker.model.RGBPixel

class DominantColorsPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        val colorFrequency = mutableMapOf<RGBPixel, Int>()
        for (pixel in pixels) {
            val quantized = RGBPixel(
                r = ((pixel.r + 8) / 16) * 16,
                g = ((pixel.g + 8) / 16) * 16,
                b = ((pixel.b + 8) / 16) * 16
            )
            colorFrequency[quantized] = colorFrequency.getOrDefault(quantized, 0) + 1
        }

        return colorFrequency.entries
            .sortedByDescending { it.value }
            .take(k)
            .map { it.key.toColor() }
    }
}