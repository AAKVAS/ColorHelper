package core.ui.composeComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import coil3.compose.AsyncImage
import core.model.ImageSource
import core.ui.theme.Dimens
import core.ui.theme.LocalColorProvider


@Composable
fun PhotoInputArea(
    imageSource: ImageSource?,
    onImageDropped: (ImageSource) -> Unit,
    onPickButtonClick: () -> Unit,
    onCloseImageButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    block: @Composable BoxScope.(ImageSource) -> Unit
) {
    imageSource?.let { source ->
        Box(
            modifier = modifier
                .padding(Dimens.paddingSmall)
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                .background(LocalColorProvider.current.onPrimary)
                .padding(Dimens.paddingXXSmall)
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                .background(LocalColorProvider.current.onPrimary)
                .height(Dimens.pickedPhotoHeight)
        ) {
            block(source)

            CloseButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Dimens.paddingSmall),
                onClick = onCloseImageButtonClick
            )
        }
    } ?: run {
        PhotoInputBox(
            onImageDropped = onImageDropped,
            onPickButtonClick = onPickButtonClick,
            modifier = modifier
        )
    }
}

@Composable
fun DisplayImage(
    source: ImageSource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    when (source) {
        is ImageSource.Path -> DisplayImage(
            model = source.value,
            contentDescription = contentDescription,
            modifier = modifier
        )
        is ImageSource.BitmapSource -> DisplayImage(
            bitmap = source.value,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

@Composable
fun DisplayImage(
    model: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
fun DisplayImage(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = modifier
    )
}