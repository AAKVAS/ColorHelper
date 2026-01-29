package ui.composeComponents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Stable
class TabSwipeState(
    initialPage: Int = 0,
    val pageCount: Int = 2
) {
    private val _selectedTabIndex = mutableIntStateOf(initialPage)
    private val _currentPageOffset = mutableFloatStateOf(0f)
    private val _isSwiping = mutableStateOf(false)

    val selectedTabIndex: Int get() = _selectedTabIndex.intValue
    val currentPageOffset: Float get() = _currentPageOffset.floatValue
    val isSwiping: Boolean get() = _isSwiping.value

    fun selectTab(index: Int) {
        _selectedTabIndex.intValue = index.coerceIn(0, pageCount - 1)
        _currentPageOffset.floatValue = 0f
    }

    fun updateSwipe(offset: Float) {
        _currentPageOffset.floatValue = offset
        _isSwiping.value = offset != 0f
    }

    fun completeSwipe() {
        val offset = _currentPageOffset.floatValue
        when {
            offset <= -0.3f && selectedTabIndex < pageCount - 1 -> {
                _selectedTabIndex.intValue = selectedTabIndex + 1
            }
            offset >= 0.3f && selectedTabIndex > 0 -> {
                _selectedTabIndex.intValue = selectedTabIndex - 1
            }
        }
        _currentPageOffset.floatValue = 0f
        _isSwiping.value = false
    }
}

@Composable
fun rememberTabSwipeState(
    initialPage: Int = 0,
    pageCount: Int = 2
): TabSwipeState {
    return remember(initialPage, pageCount) {
        TabSwipeState(initialPage, pageCount)
    }
}

fun Modifier.swipeableTabs(
    state: TabSwipeState,
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled) return@composed this

    this.pointerInput(state) {
        detectHorizontalDragGestures(
            onDragStart = { },
            onDragEnd = {
                state.completeSwipe()
            },
            onHorizontalDrag = { change, dragAmount ->
                change.consume()
                val offset = -dragAmount / size.width.toFloat()
                state.updateSwipe(offset)
            }
        )
    }
}


@Composable
fun TabSwipeContainer(
    modifier: Modifier = Modifier,
    tabCount: Int = 2,
    selectedTabIndex: Int = 0,
    onTabSelected: (Int) -> Unit,
    tabContent: @Composable (index: Int, isSelected: Boolean) -> Unit,
    pageContent: @Composable (index: Int) -> Unit
) {
    val swipeState = rememberTabSwipeState(
        initialPage = selectedTabIndex,
        pageCount = tabCount
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTabIndex) {
        if (swipeState.selectedTabIndex != selectedTabIndex) {
            swipeState.selectTab(selectedTabIndex)
        }
    }

    LaunchedEffect(swipeState.selectedTabIndex) {
        if (swipeState.selectedTabIndex != selectedTabIndex) {
            onTabSelected(swipeState.selectedTabIndex)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Верхние вкладки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(tabCount) { index ->
                val isSelected = index == swipeState.selectedTabIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            coroutineScope.launch {
                                swipeState.selectTab(index)
                                onTabSelected(index)
                            }
                        }
                ) {
                    tabContent(index, isSelected)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeableTabs(swipeState)
                .clipToBounds()
        ) {
            val offsetAnimation by animateDpAsState(
                targetValue = with(LocalDensity.current) {
                    (-swipeState.currentPageOffset * swipeState.selectedTabIndex * 100).dp
                },
                animationSpec = tween(
                    durationMillis = if (swipeState.isSwiping) 0 else 300,
                    easing = FastOutSlowInEasing
                ),
                label = "pageOffset"
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = offsetAnimation)
            ) {
                repeat(tabCount) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                    ) {
                        if (index == swipeState.selectedTabIndex ||
                            index == swipeState.selectedTabIndex - 1 ||
                            index == swipeState.selectedTabIndex + 1) {
                            pageContent(index)
                        }
                    }
                }
            }
        }
    }
}
