package utils

import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import feature.palette.photoPicker.ImageSource
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.toImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
actual fun GetImageBySource(
    imageSource: ImageSource,
    onLoad: (Image?) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(imageSource) {
        val image = withContext(Dispatchers.IO) {
            try {
                when(imageSource) {
                    is ImageSource.Path -> {
                        val bitmap = if (imageSource.value.startsWith("content://")) {
                            val uri = imageSource.value.toUri()
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            BitmapFactory.decodeFile(imageSource.value)
                        }

                        bitmap.toImage(imageSource.value).also {
                            bitmap.recycle()
                        }
                    }
                    else -> {
                        null
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
        onLoad(image)
    }
}

actual fun pasteImageFromClipboard(): ImageBitmap? {
    return null
}

@Composable
actual fun HandleClipboardPaste(onPaste: () -> Unit) {}

@Composable
actual fun HandleClipboardCopy(onCopy: () -> Unit) {}
