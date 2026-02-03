package feature.palette.photoPicker.paletteExtractor

import androidx.compose.ui.graphics.Color
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import utils.toHex
import java.util.UUID


abstract class PaletteExtractor {
    suspend fun extractFromImage(image: Image, colorCount: Int): ColorPalette {
        try {
            val colors = getPalette(image.pixels, colorCount)
            return createPalette(image.path, colors)
        } catch (e: Exception) {
            throw PaletteExtractionException("Failed to extract palette from ${image.path}", e)
        }
    }

    abstract suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int = 20): List<Color>

    private fun createPalette(path: String, dominantColors: List<Color>): ColorPalette {
        val paletteUid = UUID.randomUUID().toString()
        val paletteCreatedAt = System.currentTimeMillis()

        val colors = dominantColors.map {
            ColorModel(
                uid = UUID.randomUUID().toString(),
                paletteUid = paletteUid,
                value = it.toHex(withAlpha = true),
                createdAt = System.currentTimeMillis()
            )
        }

        val palette = ColorPalette(
            uid = paletteUid,
            name = extractFileName(path),
            colors = colors,
            createdAt = paletteCreatedAt
        )
        return palette
    }

    private fun extractFileName(path: String): String {
        val separator = if (path.contains("/")) "/" else "\\"
        return path.substringAfterLast(separator).substringBeforeLast(".")
    }
}

data class Image(
    val path: String,
    val width: Int,
    val height: Int,
    val pixels: List<RGBPixel>
)

enum class ExtractionMethod {
    DOMINANT_COLORS,
    K_MEANS,
    MEDIAN_CUT,
    COLOR_THIEF
}

class PaletteExtractionException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

data class RGBPixel(
    val r: Int,
    val g: Int,
    val b: Int
) {
    fun toColor(): Color = Color(r / 255f, g / 255f, b / 255f)
}