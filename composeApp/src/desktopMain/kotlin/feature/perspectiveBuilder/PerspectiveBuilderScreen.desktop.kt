package feature.perspectiveBuilder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import ui.composeComponents.CopyButton
import utils.copyImageToClipboard
import utils.toBufferedImage

@Composable
actual fun ExportButton(modifier: Modifier, onClick: () -> Unit) {
    CopyButton(modifier = modifier) {
        onClick()
    }
}

@Composable
actual fun ExportPerspectiveScene(bitmap: ImageBitmap) {
    LaunchedEffect(bitmap) {
        copyImageToClipboard(bitmap.toBufferedImage())
    }
}