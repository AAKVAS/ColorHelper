package feature.palette.photoPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import coil3.compose.AsyncImage
import com.example.Res
import com.example.color_count
import com.example.color_thief_algorithm
import com.example.`continue`
import com.example.dominant_colors
import com.example.extract_palette
import com.example.extraction_method
import com.example.k_means_clustering
import com.example.median_cut
import com.example.palette_generated
import com.example.selected_photo
import feature.palette.model.ColorModel
import feature.palette.photoPicker.domain.ExtractionMethod
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.CloseButton
import ui.composeComponents.ImagePicker
import ui.composeComponents.PhotoInputBox
import ui.composeComponents.SimpleButton
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import utils.GetImageBySource
import utils.HandleClipboardPaste
import utils.pasteImageFromClipboard
import utils.toColor

sealed interface ImageSource {
    data class Path(val value: String): ImageSource
    data class BitmapSource(val value: ImageBitmap): ImageSource
}

@Composable
fun PhotoPickerScreen(
    component: PhotoPickerComponent,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState()
    var imageSource by remember { mutableStateOf<ImageSource?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }
    var colorCount by remember { mutableIntStateOf(5) }
    var extractionMethod by remember { mutableStateOf(ExtractionMethod.DOMINANT_COLORS) }
    val scrollState = rememberScrollState()

    HandleClipboardPaste {
        imageSource = pasteImageFromClipboard()?.let {
            ImageSource.BitmapSource(it)
        }
    }

    LaunchedEffect(state) {
        state.value.selectedImagePath?.let { uri ->
            imageSource = ImageSource.Path(uri)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.paddingMedium)
                .fillMaxSize()
                .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                .background(LocalColorProvider.current.primaryContainer)
                .padding(Dimens.paddingSmall)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (windowSize.height < Dimens.lowLargeWindowHeight) {
                Column(
                    modifier = Modifier.wrapContentHeight().wrapContentWidth()
                ) {
                    CloseButton {
                        component.onCancel()
                    }
                    Row(
                        modifier = modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(LocalColorProvider.current.primaryContainer),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PhotoInputArea(
                            imageSource = imageSource,
                            onImageDropped = { source ->
                                imageSource = source
                            },
                            onPickButtonClick = {
                                showImagePicker = true
                            },
                            onCloseImageButtonClick = {
                                imageSource = null
                            },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsSection(
                            colorCount = colorCount,
                            onColorCountChange = { colorCount = it },
                            extractionMethod = extractionMethod,
                            onMethodChange = { extractionMethod = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.wrapContentHeight().wrapContentWidth()
                ) {
                    CloseButton {
                        component.onCancel()
                    }

                    PhotoInputArea(
                        imageSource = imageSource,
                        onImageDropped = { source ->
                            imageSource = source
                        },
                        onPickButtonClick = {
                            showImagePicker = true
                        },
                        onCloseImageButtonClick = {
                            imageSource = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SettingsSection(
                        colorCount = colorCount,
                        onColorCountChange = { colorCount = it },
                        extractionMethod = extractionMethod,
                        onMethodChange = { extractionMethod = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            state.value.extractedPalette?.let { palette ->
                if (palette.colors.isNotEmpty()) {
                    GeneratedPaletteColors(palette.colors)
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = Dimens.paddingMedium)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                imageSource?.let {
                    SimpleButton(
                        text = stringResource(Res.string.extract_palette)
                    ) {
                        imageSource?.let {
                            component.loadImage()
                        }
                    }
                }
                if (state.value.extractedPalette != null) {
                    SimpleButton(
                        text = stringResource(Res.string.`continue`)
                    ) {
                        component.onSavePalette()
                    }
                }
            }

            if (showImagePicker) {
                ImagePicker { source ->
                    imageSource = source
                    showImagePicker = false
                }
            }
            if (state.value.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.paddingSmall)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {}
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Dimens.circularProgressIndicatorSize)
                            .align(Alignment.Center),
                        strokeWidth = Dimens.paddingXXSmall
                    )
                    CloseButton(
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        component.onCancel()
                    }
                }
            }
            if (state.value.loadImage) {
                imageSource?.let { source ->
                    GetImageBySource(source) { image ->
                        image?.let {
                            component.extractImage(it, colorCount, extractionMethod)
                        } ?: run {
                            component.imageNotLoaded()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoInputArea(
    imageSource: ImageSource?,
    onImageDropped: (ImageSource) -> Unit,
    onPickButtonClick: () -> Unit,
    onCloseImageButtonClick: () -> Unit,
    modifier: Modifier = Modifier
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
            when (source) {
                is ImageSource.Path -> AsyncImage(
                    model = source.value,
                    contentDescription = stringResource(Res.string.selected_photo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.pickedPhotoHeight)
                )
                is ImageSource.BitmapSource -> {
                    androidx.compose.foundation.Image(
                        bitmap = source.value,
                        contentDescription = stringResource(Res.string.selected_photo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.pickedPhotoHeight)
                    )
                }
            }
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
fun SettingsSection(
    colorCount: Int,
    onColorCountChange: (Int) -> Unit,
    extractionMethod: ExtractionMethod,
    onMethodChange: (ExtractionMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = Dimens.paddingSmall),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingRegular)
    ) {
        Text(
            text = stringResource(Res.string.color_count, colorCount.toString()),
            color = LocalColorProvider.current.onBackground
        )
        Slider(
            value = colorCount.toFloat(),
            onValueChange = { onColorCountChange(it.toInt()) },
            valueRange = 3f..15f,
            steps = 11,
            colors = SliderDefaults.colors(
                thumbColor = LocalColorProvider.current.primary,
                activeTrackColor = LocalColorProvider.current.primary,
                inactiveTrackColor = LocalColorProvider.current.primaryVariant,
                inactiveTickColor = LocalColorProvider.current.primaryWeak,
                activeTickColor = LocalColorProvider.current.primaryVariant
            ),
            modifier = Modifier.height(Dimens.sliderHeight)
        )
        Text(
            text = stringResource(Res.string.extraction_method),
            color = LocalColorProvider.current.onBackground
        )
        ExtractionMethod.entries.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onMethodChange(method) }
                    .wrapContentSize()
            ) {
                RadioButton(
                    selected = extractionMethod == method,
                    onClick = { onMethodChange(method) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = LocalColorProvider.current.primary,
                        unselectedColor = LocalColorProvider.current.onSurface,
                    ),
                    modifier = Modifier.size(Dimens.radioButtonSize)
                )
                Text(
                    text = method.getMethodName(),
                    modifier = Modifier.padding(start = Dimens.paddingSmall),
                    color = LocalColorProvider.current.onBackground
                )
            }
        }
    }

}

@Composable
fun GeneratedPaletteColors(
    colors: List<ColorModel>
) {
    Text(
        modifier = Modifier.padding(top = Dimens.paddingSmall).fillMaxWidth(),
        text = stringResource(Res.string.palette_generated),
        color = LocalColorProvider.current.onBackground,
        textAlign = TextAlign.Center
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingSmall),
        horizontalArrangement = Arrangement.Center
    ) {
        items(colors) { item ->
            Box(
                modifier = Modifier.size(Dimens.buttonHeight)
                    .padding(Dimens.paddingSmall)
                    .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                    .background(LocalColorProvider.current.primaryContainer)
                    .padding(Dimens.paddingXXSmall)
                    .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
                    .background(item.value.toColor())
            )
        }
    }
}

@Composable
fun ExtractionMethod.getMethodName(): String {
    return when(this) {
        ExtractionMethod.DOMINANT_COLORS -> stringResource(Res.string.dominant_colors)
        ExtractionMethod.K_MEANS -> stringResource(Res.string.k_means_clustering)
        ExtractionMethod.MEDIAN_CUT -> stringResource(Res.string.median_cut)
        ExtractionMethod.COLOR_THIEF -> stringResource(Res.string.color_thief_algorithm)
    }
}