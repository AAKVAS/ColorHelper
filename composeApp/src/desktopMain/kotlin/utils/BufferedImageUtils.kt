package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import feature.imageBusket.data.ImageData
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Composable
fun rememberImageBitmap(bufferedImage: BufferedImage): ImageBitmap {
    return remember(bufferedImage) {
        bufferedImage.toImageBitmap()
    }
}

fun BufferedImage.toImageBitmap(): ImageBitmap {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(this, "PNG", outputStream)
    val pngBytes = outputStream.toByteArray()

    return ByteArrayInputStream(pngBytes).readAllBytes().decodeToImageBitmap()
}

fun ImageBitmap.toBufferedImage(): BufferedImage {
    val skiaBitmap = this.asSkiaBitmap()
    val width = this.width
    val height = this.height

    require(width > 0 && height > 0) { "Bitmap has invalid dimensions: ${width}x${height}" }

    val dstInfo = ImageInfo(
        width = width,
        height = height,
        colorType = ColorType.RGBA_8888,
        alphaType = ColorAlphaType.UNPREMUL
    )

    val byteArray = skiaBitmap.readPixels(
        dstInfo = dstInfo,
        dstRowBytes = width * 4,
        srcX = 0,
        srcY = 0
    ) ?: throw IllegalStateException("Failed to read pixels from Skia bitmap")

    require(byteArray.size >= width * height * 4) {
        "ByteArray size ${byteArray.size} is too small for ${width}x$height image"
    }

    val pixels = IntArray(width * height)

    for (i in pixels.indices) {
        val index = i * 4
        val r = byteArray[index].toInt() and 0xFF
        val g = byteArray[index + 1].toInt() and 0xFF
        val b = byteArray[index + 2].toInt() and 0xFF
        val a = byteArray[index + 3].toInt() and 0xFF
        pixels[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).apply {
        setRGB(0, 0, width, height, pixels, 0, width)
    }
}

fun getImageFromClipboard(): ImageData? {
    return try {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            clipboard.getData(DataFlavor.imageFlavor) as? ImageData
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

fun copyImageToClipboard(image: BufferedImage): Boolean {
    return try {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val transferable = ImageTransferable(image)
        clipboard.setContents(transferable, null)
        true
    } catch (e: Exception) {
        false
    }
}

class ImageTransferable(private val image: BufferedImage) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(DataFlavor.imageFlavor)
    }

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        return flavor == DataFlavor.imageFlavor
    }

    override fun getTransferData(flavor: DataFlavor): Any {
        if (!isDataFlavorSupported(flavor)) {
            throw UnsupportedFlavorException(flavor)
        }
        return image
    }
}