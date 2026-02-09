package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import feature.imageBusket.data.ImageData
import org.jetbrains.compose.resources.decodeToImageBitmap
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