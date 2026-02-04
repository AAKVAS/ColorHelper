package feature.palette.photoPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.model.RGBPixel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


@Composable
actual fun GetImageByPath(path: String, onLoad: (Image?) -> Unit) {
    LaunchedEffect(path) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(path)
                if (!file.exists()) {
                    return@withContext
                }

                val bufferedImage = ImageIO.read(file) ?: return@withContext
                onLoad(bufferedImage.toImage(path))
            } catch (_: Exception) {
                onLoad(null)
            }
        }
    }
}

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