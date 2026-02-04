package feature.palette.photoPicker
import android.graphics.BitmapFactory
import android.graphics.Color
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import coil3.Bitmap
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.model.RGBPixel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun GetImageByPath(
    path: String,
    onLoad: (Image?) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(path) {
        val image = withContext(Dispatchers.IO) {
            try {
                val bitmap = if (path.startsWith("content://")) {
                    val uri = path.toUri()
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    BitmapFactory.decodeFile(path)
                }

                bitmap.toImage(path).also {
                    bitmap.recycle()
                }
            } catch (e: Exception) {
                null
            }
        }
        onLoad(image)
    }
}


fun Bitmap.toImage(path: String, sampleStep: Int = 4): Image {
    val pixels = mutableListOf<RGBPixel>()
    val width = this.width
    val height = this.height

    for (x in 0 until width step sampleStep) {
        for (y in 0 until height step sampleStep) {
            val color = getPixel(x, y)
            pixels.add(
                RGBPixel(
                    r = Color.red(color),
                    g = Color.green(color),
                    b = Color.blue(color)
                )
            )
        }
    }

    return Image(path, width, height, pixels)
}