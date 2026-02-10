package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import feature.palette.photoPicker.ImageSource
import feature.palette.photoPicker.model.Image

@Composable
expect fun GetImageBySource(imageSource: ImageSource, onLoad: (Image?) -> Unit)

@Composable
expect fun HandleClipboardPaste(onPaste: () -> Unit)

@Composable
expect fun HandleClipboardCopy(onCopy: () -> Unit)

expect fun pasteImageFromClipboard(): ImageBitmap?