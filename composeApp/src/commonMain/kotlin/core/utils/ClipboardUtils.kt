package core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import core.model.Image
import core.model.ImageSource

@Composable
expect fun GetImageBySource(imageSource: ImageSource, onLoad: (Image?) -> Unit)

@Composable
expect fun HandleClipboardPaste(onPaste: () -> Unit)

@Composable
expect fun HandleClipboardCopy(onCopy: () -> Unit)

expect fun pasteImageFromClipboard(): ImageBitmap?