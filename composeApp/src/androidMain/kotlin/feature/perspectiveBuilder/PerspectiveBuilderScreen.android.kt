package feature.perspectiveBuilder

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.Res
import com.example.title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.ShareButton
import java.io.File
import java.io.FileOutputStream

@Composable
actual fun ExportButton(modifier: Modifier, onClick: () -> Unit) {
    ShareButton(modifier = modifier) {
        onClick()
    }
}

@Composable
actual fun ExportPerspectiveScene(bitmap: ImageBitmap) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val title = stringResource(Res.string.title)
    val failed = stringResource(Res.string.title)

    LaunchedEffect(bitmap) {
        scope.launch {
            try {
                val file = File(context.cacheDir, "perspective.png")

                if (file.exists()) {
                    file.delete()
                }

                val androidBitmap = bitmap.asAndroidBitmap()

                FileOutputStream(file).use { out ->
                    androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(
                    Intent.createChooser(shareIntent, title)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context, failed, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
