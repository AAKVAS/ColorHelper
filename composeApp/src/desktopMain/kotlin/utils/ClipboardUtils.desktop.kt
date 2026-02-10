package utils

import LocalShortcutManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import feature.palette.photoPicker.ImageSource
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.toImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO


@Composable
actual fun GetImageBySource(imageSource: ImageSource, onLoad: (Image?) -> Unit) {
    LaunchedEffect(imageSource) {
        withContext(Dispatchers.IO) {
            try {
                when(imageSource) {
                    is ImageSource.Path -> {
                        val file = File(imageSource.value)
                        if (!file.exists()) {
                            return@withContext
                        }

                        val bufferedImage = ImageIO.read(file) ?: return@withContext
                        onLoad(bufferedImage.toImage(imageSource.value))
                    }
                    is ImageSource.BitmapSource -> {
                        onLoad(imageSource.value.toImage())
                    }
                }
            } catch (_: Exception) {
                onLoad(null)
            }
        }
    }
}


actual fun pasteImageFromClipboard(): ImageBitmap? {
    return getImageFromClipboard()?.toImageBitmap()
}

@Composable
actual fun HandleClipboardPaste(onPaste: () -> Unit) {
    val shortcutManager = LocalShortcutManager.current

    LaunchedEffect(shortcutManager) {
        shortcutManager.pasteEvents.collect {
            onPaste()
        }
    }
}

@Composable
actual fun HandleClipboardCopy(onCopy: () -> Unit) {
    val shortcutManager = LocalShortcutManager.current

    LaunchedEffect(shortcutManager) {
        shortcutManager.copyEvents.collect {
            onCopy()
        }
    }
}


