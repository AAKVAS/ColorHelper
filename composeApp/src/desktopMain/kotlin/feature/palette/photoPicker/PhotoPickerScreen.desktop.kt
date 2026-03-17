package feature.palette.photoPicker

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import core.model.Image
import core.model.RGBPixel
import java.awt.image.BufferedImage
import java.util.UUID


fun BufferedImage.toImage(path: String, sampleStep: Int = 4): Image {
    val pixels = mutableListOf<RGBPixel>()
    val width = this.width
    val height = this.height

    for (x in 0 until width step sampleStep) {
        for (y in 0 until height step sampleStep) {
            val rgb = getRGB(x, y)
            pixels.add(
                RGBPixel(
                    r = (rgb shr 16) and 0xFF,
                    g = (rgb shr 8) and 0xFF,
                    b = rgb and 0xFF
                )
            )
        }
    }

    return Image(path, width, height, pixels)
}

fun ImageBitmap.toImage(sampleStep: Int = 4): Image {
    val pixels = mutableListOf<RGBPixel>()
    val width = this.width
    val height = this.height
    val path = UUID.randomUUID().toString()

    val pixelMap = this.toPixelMap()

    val newWidth = (width + sampleStep - 1) / sampleStep
    val newHeight = (height + sampleStep - 1) / sampleStep

    for (y in 0 until height step sampleStep) {
        for (x in 0 until width step sampleStep) {
            val color = pixelMap[x, y]

            val r = (color.red * 255).toInt()
            val g = (color.green * 255).toInt()
            val b = (color.blue * 255).toInt()

            pixels.add(RGBPixel(r, g, b))
        }
    }

    return Image(path, newWidth, newHeight, pixels)
}