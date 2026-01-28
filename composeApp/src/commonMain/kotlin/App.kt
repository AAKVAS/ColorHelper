import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.Res
import com.example.lab
import com.example.palettes
import feature.colorLab.ColorHelperScreen
import feature.home.RootComponent
import feature.palette.PaletteListScreen
import org.jetbrains.compose.resources.stringResource
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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = Dimens.paddingSmall
                )
            ) {
                TabUI(
                    isPortrait = isPortrait.value,
                ) { index ->
                    if (index == 0) {
                        rootComponent.navigateToPaletteList()
                    } else {
                        rootComponent.navigateToColorLab()
                    }
                }

                Children(
                    stack = rootComponent.stack,
                    modifier = Modifier,
                    animation = stackAnimation(fade()),
                ) {
                    when (val child = it.instance) {
                        is RootComponent.Child.ColorLabChild -> ColorHelperScreen(
                            component = child.component,
                            windowSize = windowSize
                        )

                        is RootComponent.Child.PaletteListChild -> PaletteListScreen(
                            component = child.component,
                            windowSize = windowSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabUI(
    isPortrait: Boolean,
    onTabClick: (Int) -> Unit
) {
    val selectedTabIndex = remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(Res.string.palettes), stringResource(Res.string.lab))

    if (isPortrait) {
        RowTabulation(
            selectedTabIndex = selectedTabIndex.value,
            tabs = tabs
        ) { index ->
            selectedTabIndex.value = index
            onTabClick(index)
        }
    } else {
        ButtonTabulation(
            selectedTabIndex = selectedTabIndex.value,
            tabs = tabs
        ) { index ->
            selectedTabIndex.value = index
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
                height = 2.dp,
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
        .padding(8.dp)
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
                vertical = 2.dp,
                horizontal = 4.dp
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            )
        ,
        color = textColor,
        fontSize = 18.sp
    )
}