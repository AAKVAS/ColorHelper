package feature.perspectiveBuilder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import core.ui.composeComponents.CopyButton
import core.utils.copyImageToClipboard
import core.utils.toBufferedImage

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