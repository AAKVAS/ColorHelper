package feature.palette.photoPicker.domain

import androidx.compose.ui.graphics.Color
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.model.RGBPixel
import utils.toHex
import utils.toHsl
import java.util.UUID


abstract class PaletteExtractor {
    suspend fun extractFromImage(image: Image, colorCount: Int): ColorPalette {
        try {
            val colors = getPalette(image.pixels, colorCount).sortColors()
            return createPalette(image.path, colors)
        } catch (e: Exception) {
            throw PaletteExtractionException("Failed to extract palette from ${image.path}", e)
        }
    }

    abstract suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int = 20): List<Color>

    private fun List<Color>.sortColors(): List<Color> {
        if (this.size <= 1) return this

        val colorsWithHSL = this.map { color ->
            val hsl = color.toHsl()
            ColorWithHSL(color, hsl)
        }

        return colorsWithHSL.sortedWith(compareBy(
            { it.hsl.first },
            { it.hsl.second },
            { it.hsl.third }
        )).map { it.color }
    }

    private data class ColorWithHSL(val color: Color, val hsl: Triple<Float, Float, Float>)

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

    protected fun colorDistanceSquared(c1: Color, c2: Color): Float {
        val deltaR = (c1.red - c2.red) * 255
        val deltaG = (c1.green - c2.green) * 255
        val deltaB = (c1.blue - c2.blue) * 255

        return deltaR * deltaR + deltaG * deltaG + deltaB * deltaB
    }

    protected fun colorDistanceSquared(p1: RGBPixel, p2: RGBPixel): Int {
        val deltaR = p1.r - p2.r
        val deltaG = p1.g - p2.g
        val deltaB = p1.b - p2.b

        return deltaR * deltaR + deltaG * deltaG + deltaB * deltaB
    }
}

enum class ExtractionMethod {
    DOMINANT_COLORS,
    K_MEANS,
    MEDIAN_CUT,
    COLOR_THIEF
}

class PaletteExtractionException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

