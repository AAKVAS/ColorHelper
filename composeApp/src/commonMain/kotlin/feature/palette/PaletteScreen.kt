package feature.palette


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Res
import com.example.copied
import com.example.delete
import com.example.harmonious_colors
import com.example.outline_delete
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import feature.palette.model.ColorModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.BackHandlerWrapper
import ui.composeComponents.BackNavigationButton
import ui.composeComponents.ColorPickerUI
import ui.composeComponents.CustomTextField
import ui.composeComponents.DeleteButton
import ui.composeComponents.DeleteColorDialog
import ui.composeComponents.DeletePaletteDialog
import ui.theme.Dimens
import ui.theme.Dimens.iconButtonSize
import ui.theme.Dimens.paddingSmall
import ui.theme.LocalColorProvider
import utils.toColor
import utils.toHex


@Composable
fun PaletteScreen(
    component: PaletteComponent,
    isPortrait: Boolean,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val showDeleteColorDialog = remember { mutableStateOf(false) }
    val deletedUid = remember { mutableStateOf<String?>(null) }
    val showDeletePaletteDialog = remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val copied = stringResource(Res.string.copied)

    val snackbarHostState = remember { SnackbarHostState() }
    val onCopyTextToClipboard: (String) -> Unit = { text ->
        scope.launch {
            clipboardManager.setText(
                AnnotatedString(text)
            )

            snackbarHostState.showSnackbar(copied)
        }
    }

    LaunchedEffect(component.labels) {
        component.labels.collect { label ->
            when(label) {
                is PaletteStore.Label.ShowMessage -> {

                }
                is PaletteStore.Label.ShowDeleteColorDialog -> {
                    showDeleteColorDialog.value = true
                    deletedUid.value = label.uid
                }
                is PaletteStore.Label.ShowDeletePaletteDialog -> {
                    showDeletePaletteDialog.value = true
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LocalColorProvider.current.onPrimary)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        focusManager.clearFocus()
                    }
                )
            }
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandlerWrapper {
            component.navigateBack()
        }

        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (isPortrait) {
                BackNavigationButton {
                    component.navigateBack()
                }
            }
            PaletteNameUI(
                state.value.palette.name,
                modifier = Modifier.weight(1.0f)
            ) { name ->
                component.updatePalette(state.value.palette.copy(name = name))
            }
            DeleteButton(
                modifier = Modifier.padding(end = Dimens.paddingXXSmall)
            ) {
                component.showDeleteDialog()
            }
        }
        PaletteColorsGrid(
            items = state.value.palette.colors,
            onColorClick = { clickedUid ->
                if (clickedUid == state.value.selectedColorUid) {
                    component.showDeleteColorDialog(clickedUid)
                } else {
                    component.updateSelectedColorUid(clickedUid)
                }
            },
            onAddColorButtonClick = {
                component.addColor("#FFFFFFFF")
            },
            selectedItemUid = state.value.selectedColorUid ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(8.dp)
        )

        if (state.value.selectedColorUid != null) {
            val colorModel = state.value.palette.colors.firstOrNull {
                it.uid == state.value.selectedColorUid
            }

            if (colorModel != null) {
                key(state.value.selectedColorUid) {
                    ColorSettings(
                        defaultColor = colorModel.value.toColor(),
                        onColorChange = { color ->
                            val newColor = colorModel.copy(value = color)
                            component.updateColor(newColor)
                        },
                        onCopyTextToClipboard = onCopyTextToClipboard
                    )
                }
                if (state.value.harmoniousColors.isNotEmpty()) {
                    HarmoniousColors(
                        items = state.value.harmoniousColors,
                        selectColor = component::selectHarmoniousColor
                    )
                }
            }
        }
    }

    if (showDeleteColorDialog.value) {
        val deletedColor = state.value.palette.colors.first { it.uid == deletedUid.value }
        DeleteColorDialog(
            onDismiss = {
                showDeleteColorDialog.value = false
            },
            onConfirm = {
                showDeleteColorDialog.value = false
                component.deleteColor(deletedColor)
            },
            itemName = deletedColor.value
        )
    }
    if (showDeletePaletteDialog.value) {
        DeletePaletteDialog(
            onDismiss = {
                showDeletePaletteDialog.value = false
            },
            onConfirm = {
                showDeletePaletteDialog.value = false
                component.deletePalette()
            },
            itemName = state.value.palette.name
        )
    }
}

@Composable
fun PaletteNameUI(
    text: String,
    modifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit
) {
    CustomTextField(
        text = text,
        modifier = modifier
            .padding(10.dp)
            .widthIn(240.dp, 300.dp)
            .height(30.dp),
        fontSize = 18.sp,
        onValueChange = onNameChanged
    )
}

@Composable
fun PaletteColorsGrid(
    items: List<ColorModel>,
    onColorClick: (String) -> Unit,
    selectedItemUid: String,
    onAddColorButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(size = Dimens.buttonHeight),
        modifier = modifier
    ) {
        items(items, key = { color -> color.uid }) { item ->
            Box(
                modifier = Modifier
                    .size(
                        Dimens.buttonHeight
                    ).clickable {
                        onColorClick(item.uid)
                    }
                    .padding(paddingSmall)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LocalColorProvider.current.primaryContainer)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(item.value.toColor()),
                    contentAlignment = Alignment.Center
            ) {
                if (item.uid == selectedItemUid) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.Center)
                            .padding(Dimens.paddingXXSmall)
                            .clickable {
                                onColorClick(item.uid)
                            }
                            .background(LocalColorProvider.current.onPrimary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.outline_delete),
                            contentDescription = stringResource(Res.string.delete),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
                            modifier = Modifier.size(iconButtonSize)
                        )
                    }
                }
            }
        }
        item {
            AddColorButton(onClick = onAddColorButtonClick)
        }
    }
}

@Composable
fun AddColorButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(Dimens.buttonHeight)
            .clickable { onClick() }
            .padding(paddingSmall)
            .clip(RoundedCornerShape(Dimens.paddingXSmall))
            .background(LocalColorProvider.current.secondary),
            contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "+",
            fontSize = 18.sp,
            color = LocalColorProvider.current.onPrimary
        )
    }
}

@Composable
fun ColorSettings(
    defaultColor: Color,
    onCopyTextToClipboard: (String) -> Unit,
    onColorChange: (String) -> Unit
) {
    val colorController = rememberColorPickerController()

    LaunchedEffect(defaultColor) {
        colorController.getColorFlow().collect {
            if (it.fromUser) {
                onColorChange(it.color.toHex())
            }
        }
    }
    ColorPickerUI(
        colorController = colorController,
        defaultColor = defaultColor,
        onCopyTextToClipboard = onCopyTextToClipboard
    )
}

@Composable
fun HarmoniousColors(
    items: List<String>,
    modifier: Modifier = Modifier,
    selectColor: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.harmonious_colors),
            color = LocalColorProvider.current.onSurface,
            fontSize = 16.sp,
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier.size(Dimens.buttonHeight)
                    .clickable {
                        selectColor(item)
                    }
                    .padding(paddingSmall)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LocalColorProvider.current.primaryContainer)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(item.toColor())
                )
            }
        }
    }
}
