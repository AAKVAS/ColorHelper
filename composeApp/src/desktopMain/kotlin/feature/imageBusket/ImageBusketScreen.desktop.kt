package feature.imageBusket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.Res
import com.example.buffered_image
import com.example.clear
import com.example.image_busket
import com.example.preview
import com.example.total
import feature.imageBusket.data.ImageData
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.CopyButton
import ui.composeComponents.DeleteButton
import ui.composeComponents.PasteButton
import ui.composeComponents.SimpleButton
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import utils.copyImageToClipboard
import utils.getImageFromClipboard
import utils.rememberImageBitmap
import kotlin.math.max
import kotlin.math.min

@Composable
actual fun ImageBusketScreen(
    modifier: Modifier,
    component: ImageBusketComponent,
    windowSize: DpSize
) {
    val state = component.state.collectAsState()
    val isPortrait = remember(windowSize) {
        derivedStateOf {
            windowSize.width < windowSize.height
        }
    }
    var imageInBuffer by remember { mutableStateOf<ImageData?>(null) }
    var lastClipboardHash by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            val currentImage = getImageFromClipboard()
            val currentHash = currentImage?.hashCode() ?: 0

            if (currentHash != lastClipboardHash) {
                lastClipboardHash = currentHash
                imageInBuffer = currentImage
            }

            delay(500)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingSmall),
    ) {
        val itemsCount = state.value.items.count()
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = Dimens.paddingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.total, itemsCount),
                color = LocalColorProvider.current.onSurface,
                fontSize = Dimens.smallTextSize
            )
            SimpleButton(text = stringResource(Res.string.clear)) {
                component.clear()
            }
        }
        val selectedIndex = state.value.selectedImageIndex
        if (!isPortrait.value) {
            Row(modifier) {
                ImageInBusketColumn(
                    images = state.value.items,
                    imageInBuffer = imageInBuffer,
                    modifier = Modifier.wrapContentWidth(),
                    onItemClick = { index ->
                        component.changeSelectedImageIndex(index)
                    },
                    onDeleteImage = { index ->
                        component.deleteImage(index)
                    },
                    onPasteButtonClick = { image ->
                        component.addImage(image)
                    }
                )
                if (selectedIndex != -1) {
                    ImagePreview(
                        image = state.value.items[selectedIndex],
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        } else {
            ImageInBusketColumn(
                images = state.value.items,
                imageInBuffer = imageInBuffer,
                modifier = Modifier.fillMaxSize(),
                onItemClick = { index ->
                    component.changeSelectedImageIndex(index)
                },
                onDeleteImage = { index ->
                    component.deleteImage(index)
                },
                onPasteButtonClick = { image ->
                    component.addImage(image)
                }
            )
        }
    }
}

@Composable
fun ImageInBusketColumn(
    images: List<ImageData>,
    imageInBuffer: ImageData?,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    onDeleteImage: (Int) -> Unit,
    onPasteButtonClick: (ImageData) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        if (imageInBuffer != null) {
            item {
                ImageInBuffer(
                    image = imageInBuffer,
                    modifier = Modifier
                        .width(Dimens.bufferedPhotoWidth)
                        .padding(vertical = Dimens.paddingXSmall),
                    pasteFromClipboard = onPasteButtonClick
                )
            }
        }
        itemsIndexed(images) { index, item ->
            ImageInBusket(
                image = item,
                modifier = Modifier.padding(vertical = Dimens.paddingXXSmall),
                copyToClipBoard = { imageData ->
                    copyImageToClipboard(imageData)
                },
                onItemClick = {
                    onItemClick(index)
                },
                onDeleteImage = {
                    onDeleteImage(index)
                }
            )
        }
    }
}

@Composable
fun ImageInBusket(
    image: ImageData,
    modifier: Modifier = Modifier,
    copyToClipBoard: (ImageData) -> Unit,
    onItemClick: () -> Unit,
    onDeleteImage: () -> Unit
) {
    val bitmap = rememberImageBitmap(image)
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                .background(LocalColorProvider.current.onBackground)
                .padding(Dimens.paddingXXSmall)
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                .width(Dimens.bufferedPhotoWidth)
                .height(Dimens.bufferedPhotoHeight)
                .background(Color.White.copy(alpha = 0.5f))
        ) {
            Image(
                bitmap = bitmap,
                contentDescription = stringResource(Res.string.image_busket),
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                    .clickable { onItemClick() },
                contentScale = ContentScale.Crop
            )
            CopyButton(
                modifier = Modifier
                    .padding(Dimens.paddingXSmall)
                    .align(alignment = Alignment.Center)
                    .drawWithCache {
                        onDrawWithContent {
                            drawCircle(
                                color = Color.Black.copy(alpha = 0.4f),
                                radius = size.minDimension / 2 + 4.dp.toPx(),
                                center = center,
                            )
                            drawContent()
                        }
                    }
            ) {
                copyToClipBoard(image)
            }
        }
        DeleteButton(onClick = onDeleteImage)
    }
}

@Composable
fun ImageInBuffer(
    image: ImageData,
    modifier: Modifier = Modifier,
    pasteFromClipboard: (ImageData) -> Unit,
) {
    val bitmap = rememberImageBitmap(image)
    Box(
        modifier = modifier.height(Dimens.bufferedPhotoHeight)
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = stringResource(Res.string.buffered_image),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize)),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        PasteButton(
            modifier = Modifier.align(alignment = Alignment.Center)
        ) {
            pasteFromClipboard(image)
        }
    }
}

@Composable
fun ImagePreview(
    image: ImageData,
    modifier: Modifier
) {
    val bitmap = rememberImageBitmap(image)
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val defaultOffset = remember { Offset.Zero }

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Scroll) {
                            val scrollAmount = event.changes.first().scrollDelta.y
                            val zoomFactor = if (scrollAmount > 0) 1.1f else 0.9f
                            scale = (scale * zoomFactor).coerceIn(0.1f, 5f)
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                )
            },
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = stringResource(Res.string.preview),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            scale = 1f
                            offset = defaultOffset
                        }
                    )
                }
                .clipToBounds()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .background(Color.LightGray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Fit
        )
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MinusButton(
                modifier = Modifier.padding(horizontal = Dimens.paddingXSmall),
            ) {
                scale = max(0.1f, scale - 0.1f)
            }
            PlusButton(
                modifier = Modifier.padding(horizontal = Dimens.paddingXSmall),
            ) {
                scale = min(5f, scale + 0.1f)
            }
            Text(
                text = "${(scale * 100).toInt()}%",
                color = LocalColorProvider.current.onSurface,
                fontSize = Dimens.mediumTextSize
            )
        }
    }
}

@Composable
fun MinusButton(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Text(
        text = "-",
        color = LocalColorProvider.current.onSurface,
        fontSize = Dimens.largeTextSize,
        modifier = modifier.clickable {
            onItemClick()
        }
    )
}

@Composable
fun PlusButton(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Text(
        text = "+",
        color = LocalColorProvider.current.onSurface,
        fontSize = Dimens.largeTextSize,
        modifier = modifier.clickable {
            onItemClick()
        }
    )
}
