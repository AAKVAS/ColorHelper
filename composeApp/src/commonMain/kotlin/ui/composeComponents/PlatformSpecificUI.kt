package ui.composeComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import feature.palette.photoPicker.ImageSource

@Composable
expect fun TooltipWrapper(
    modifier: Modifier = Modifier,
    tooltip: @Composable () -> Unit,
    content: @Composable () -> Unit
)

@Composable
expect fun BackHandlerWrapper(
    enabled: Boolean = true,
    callback: () -> Unit
)

@Composable
expect fun SetupStatusBar()


@Composable
expect fun ImagePicker(onImagePicked: (ImageSource?) -> Unit)


@Composable
expect fun PhotoInputBox(
    modifier: Modifier = Modifier,
    onImageDropped: (ImageSource) -> Unit,
    onPickButtonClick: () -> Unit
)