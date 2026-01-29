package feature.palette

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import feature.palette.PaletteListStore.Label
import feature.palette.model.ColorPalette
import ui.composeComponents.DeleteButton
import ui.composeComponents.DeletePaletteDialog
import ui.composeComponents.RoundedAddButton
import ui.theme.Dimens
import ui.theme.LocalColorProvider

@Composable
fun PaletteListScreen(
    component: PaletteListComponent,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState()
    val showDeletePaletteDialog = remember { mutableStateOf(false) }
    val deletedPalette = remember { mutableStateOf<ColorPalette?>(null) }

    LaunchedEffect(component.labels) {
        component.labels.collect { label ->
            when(label) {
                is Label.ShowMessage -> {}
                is Label.ShowEditPage -> {
                    component.showEditComponent(label.palette)
                }
                is Label.ShowDeleteDialog -> {
                    showDeletePaletteDialog.value = true
                    deletedPalette.value = label.palette
                }
            }
        }
    }

    val isPortrait = remember(windowSize) {
        derivedStateOf {
            windowSize.width < windowSize.height
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { _ ->
        if (isPortrait.value) {
            Children(
                stack = component.stack,
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalColorProvider.current.onPrimary),
                animation = stackAnimation(fade()),
            ) {
                when (val child = it.instance) {
                    is PaletteListComponent.Child.PaletteChild -> {
                        PaletteScreen(
                            component = child.component,
                            isPortrait = isPortrait.value,
                        )
                    }

                    else -> {
                        PaletteList(
                            items = state.value.items,
                            onItemClick = component::showEditComponent,
                            onAddButtonClick = component::onAddButtonClicked,
                            onDeleteButtonClick = component::showDeleteMessage
                        )
                    }
                }
            }
        } else {
            Row(modifier = modifier) {
                PaletteList(
                    items = state.value.items,
                    onItemClick = component::showEditComponent,
                    onAddButtonClick = component::onAddButtonClicked,
                    onDeleteButtonClick = component::showDeleteMessage
                )
                Children(
                    stack = component.stack,
                    modifier = Modifier.weight(1.0f),
                    animation = stackAnimation(fade()),
                ) {
                    when (val child = it.instance) {
                        is PaletteListComponent.Child.PaletteChild -> {
                            PaletteScreen(
                                component = child.component,
                                isPortrait = isPortrait.value,
                            )
                        }

                        else -> {
                            Spacer(
                                Modifier
                                    .fillMaxSize()
                                    .background(LocalColorProvider.current.onPrimary)
                            )
                        }
                    }
                }
            }
        }

        if (showDeletePaletteDialog.value) {
            DeletePaletteDialog(
                onDismiss = {
                    showDeletePaletteDialog.value = false
                },
                onConfirm = {
                    showDeletePaletteDialog.value = false
                    component.deletePalette(deletedPalette.value!!)
                },
                itemName = deletedPalette.value!!.name
            )
        }
    }
}

@Composable
fun PaletteList(
    items: List<ColorPalette>,
    onItemClick: (ColorPalette) -> Unit,
    onAddButtonClick: () -> Unit,
    onDeleteButtonClick: (ColorPalette) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedPaletteUid = remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .background(LocalColorProvider.current.onPrimary)
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .width(350.dp)
                .fillMaxHeight(1.0f)
        ) {
            items(items, key = { palette -> palette.uid }) { palette ->
                PaletteItem(
                    palette = palette,
                    isFocused = selectedPaletteUid.value == palette.uid,
                    onItemClick = { param ->
                        onItemClick(param)
                        selectedPaletteUid.value = param.uid
                    },
                    onDeleteButtonClick = onDeleteButtonClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
        RoundedAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = Dimens.paddingSmall, bottom = Dimens.paddingSmall),
        ) {
            onAddButtonClick()
        }
    }
}

@Composable
fun PaletteItem(
    palette: ColorPalette,
    onItemClick: (ColorPalette) -> Unit,
    onDeleteButtonClick: (ColorPalette) -> Unit,
    isFocused: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                onItemClick(palette)
            }
            .border(
                width = 2.dp,
                color =
                    if (isFocused) {
                        LocalColorProvider.current.primary
                    } else {
                        LocalColorProvider.current.primaryContainer
                    },
                shape = RoundedCornerShape(4.dp)
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = palette.name,
            color = LocalColorProvider.current.primary,
            modifier = Modifier
                .padding(12.dp)
                .width(260.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 16.sp
        )
        //TODO 3 first color view
        DeleteButton(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            onDeleteButtonClick(palette)
        }
    }
}
