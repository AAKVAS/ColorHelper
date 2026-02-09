import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import com.example.Res
import com.example.image_busket
import com.example.lab
import com.example.palettes
import feature.colorLab.ColorLabScreen
import feature.home.RootComponent
import feature.imageBusket.ImageBusketScreen
import feature.palette.PaletteListScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.MenuButton
import ui.theme.AppTheme
import ui.theme.Dimens
import ui.theme.LocalColorProvider

@Composable
fun App(
    rootComponent: RootComponent,
    windowSize: DpSize
) {
    AppTheme {
        val isPortrait = remember(windowSize) {
            derivedStateOf {
                windowSize.width < windowSize.height
            }
        }

        val tabPageComponents = rootComponent.children
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { tabPageComponents.size }
        )
        val activePageIndex = remember { mutableIntStateOf(0) }
        LaunchedEffect(activePageIndex.intValue) {
            pagerState.animateScrollToPage(activePageIndex.intValue)
        }
        LaunchedEffect(pagerState.currentPage) {
            activePageIndex.intValue = pagerState.currentPage
        }
        val isSceneInteracting = remember { mutableStateOf(false) }
        val tabs = listOf(
            stringResource(Res.string.palettes),
            stringResource(Res.string.lab),
            stringResource(Res.string.image_busket),
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars
        ) { innerPadding ->
            if (isPortrait.value || windowSize.height > Dimens.lowLargeWindowHeight) {
                Column(
                    modifier = Modifier
                        .background(LocalColorProvider.current.onPrimary)
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = Dimens.paddingSmall
                        )
                ) {
                    TabUI(
                        tabs = tabs,
                        isPortrait = isPortrait.value,
                        selectedTabIndex = pagerState.currentPage
                    ) { index ->
                        activePageIndex.intValue = index
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { change, _ ->
                                    change.consume()
                                }
                            },
                        userScrollEnabled = !isSceneInteracting.value
                    ) { page ->
                        when (val child = tabPageComponents[page]) {
                            is RootComponent.Child.PaletteListChild -> PaletteListScreen(
                                component = child.component,
                                windowSize = windowSize
                            )
                            is RootComponent.Child.ColorLabChild -> ColorLabScreen(
                                component = child.component,
                                windowSize = windowSize,
                                onSceneInteractionChange = { interacting ->
                                    isSceneInteracting.value = interacting
                                }
                            )
                            is RootComponent.Child.ImageBusketChild -> ImageBusketScreen(
                                component = child.component,
                                windowSize = windowSize,
                            )
                        }
                    }
                }
            } else {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    modifier = Modifier
                        .background(LocalColorProvider.current.onPrimary)
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize(),
                    drawerContent = {
                        VerticalNavModalSheet(
                            tabs = tabs,
                            selectedItemIndex = activePageIndex.intValue,
                            onItemClick = { index ->
                                scope.launch { drawerState.close() }
                                activePageIndex.intValue = index
                            }
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .background(LocalColorProvider.current.onPrimary)
                            .fillMaxSize()
                    ) {
                        VerticalNavPanel(
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight()
                        ) {
                            scope.launch { drawerState.open() }
                        }
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.0f)
                                .padding(bottom = Dimens.paddingSmall),
                            userScrollEnabled = false
                        ) { page ->
                            when (val child = tabPageComponents[page]) {
                                is RootComponent.Child.PaletteListChild -> PaletteListScreen(
                                    component = child.component,
                                    windowSize = windowSize
                                )
                                is RootComponent.Child.ColorLabChild -> ColorLabScreen(
                                    component = child.component,
                                    windowSize = windowSize,
                                    onSceneInteractionChange = { interacting ->
                                        isSceneInteracting.value = interacting
                                    }
                                )
                                is RootComponent.Child.ImageBusketChild -> ImageBusketScreen(
                                    component = child.component,
                                    windowSize = windowSize,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabUI(
    tabs: List<String>,
    selectedTabIndex: Int,
    isPortrait: Boolean,
    onTabClick: (Int) -> Unit
) {
    if (isPortrait) {
        RowTabulation(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs
        ) { index ->
            onTabClick(index)
        }
    } else {
        ButtonTabulation(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs
        ) { index ->
            onTabClick(index)
        }
    }
}

@Composable
fun RowTabulation(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabClick: (Int) -> Unit
) {
    SecondaryTabRow(
        selectedTabIndex = 0,
        modifier = Modifier.fillMaxWidth(),
        containerColor = LocalColorProvider.current.primaryContainer,
        indicator = @Composable {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedTabIndex
                ),
                height = Dimens.paddingXXSmall,
                color = LocalColorProvider.current.primary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabClick(index)
                },
                text = { Text(title) },
                selectedContentColor = LocalColorProvider.current.primary,
                unselectedContentColor = LocalColorProvider.current.onBackground
            )
        }
    }
}

@Composable
fun ButtonTabulation(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabClick: (Int) -> Unit
) {
    Row(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .background(LocalColorProvider.current.primaryContainer)
        .padding(Dimens.paddingSmall)
    ) {
        tabs.forEachIndexed { index, tab ->
            TabButton(
                text = tab,
                onTabClick = {
                    onTabClick(index)
                },
                selected = selectedTabIndex == index
            )
        }
    }
}

@Composable
fun TabButton(
    text: String,
    onTabClick: () -> Unit,
    selected: Boolean
) {
    var textColor: Color
    var borderColor: Color
    if (selected) {
        textColor = LocalColorProvider.current.primary
        borderColor = LocalColorProvider.current.primary
    } else {
        textColor = LocalColorProvider.current.onBackground
        borderColor = LocalColorProvider.current.onBackground
    }

    Text(
        text = text,
        modifier = Modifier
            .clickable {
                onTabClick()
            }
            .padding(
                vertical = Dimens.paddingXXSmall,
                horizontal = Dimens.paddingXSmall
            )
            .border(
                width = Dimens.smallestPadding,
                color = borderColor,
                shape = RoundedCornerShape(Dimens.roundedCornerShapeSize)
            )
            .padding(
                vertical = Dimens.paddingXSmall,
                horizontal = Dimens.paddingSmall
            )
        ,
        color = textColor,
        fontSize = Dimens.textSize
    )
}

@Composable
fun VerticalNavPanel(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(LocalColorProvider.current.primaryContainer)
    ) {
        MenuButton(
            modifier = Modifier
                .padding(top = Dimens.paddingSmall)
                .align(Alignment.TopCenter)
        ) {
            onButtonClick()
        }
    }
}

@Composable
fun VerticalNavModalSheet(
    tabs: List<String>,
    selectedItemIndex: Int,
    onItemClick: (Int) -> Unit,
) {
    ModalDrawerSheet(
        drawerContainerColor = LocalColorProvider.current.primaryContainer,
        drawerContentColor = LocalColorProvider.current.primaryContainer,
        drawerShape = RoundedCornerShape(Dimens.roundedCornerShapeSize),
        modifier = Modifier.width(Dimens.modalDrawerSheetWidth)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens.paddingXSmall)
        ) {
            tabs.forEachIndexed { index, item ->
                CustomDrawerItem(
                    text = item,
                    selected = selectedItemIndex == index,
                    onClick = { onItemClick(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Dimens.paddingSmall,
                            vertical = Dimens.paddingXSmall
                        )
                )
            }
        }
    }
}


@Composable
fun CustomDrawerItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        LocalColorProvider.current.primary.copy(alpha = 0.2f)
    } else {
        Color.Transparent
    }

    val textColor = if (selected) {
        LocalColorProvider.current.onBackground
    } else {
        LocalColorProvider.current.onSurface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = Dimens.paddingSmall,
                vertical = Dimens.paddingXSmall
            )
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = Dimens.smallTextSize,
            modifier = Modifier.fillMaxWidth().padding(end = Dimens.paddingXLarge)
        )
    }
}