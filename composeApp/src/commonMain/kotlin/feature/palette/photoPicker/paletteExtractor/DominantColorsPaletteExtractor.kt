package feature.palette.photoPicker.paletteExtractor

import androidx.compose.ui.graphics.Color

class DominantColorsPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        val quantizedPixels = pixels.map { pixel ->
            RGBPixel(
                r = (pixel.r / 16) * 16,
                g = (pixel.g / 16) * 16,
                b = (pixel.b / 16) * 16
            )
        }

        val colorFrequency = quantizedPixels
            .groupingBy { it }
            .eachCount()

        return colorFrequency.entries
            .sortedByDescending { it.value }
            .take(k)
            .map { it.key.toColor() }
    }
}