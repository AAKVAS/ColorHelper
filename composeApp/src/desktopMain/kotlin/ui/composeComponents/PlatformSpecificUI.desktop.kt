package ui.composeComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun TooltipWrapper(
    modifier: Modifier,
    tooltip: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    TooltipArea(
        tooltip = tooltip,
        modifier = modifier
    ) {
        content()
    }
}


@Composable
actual fun BackHandlerWrapper(
    enabled: Boolean,
    callback: () -> Unit
) {

}


@Composable
actual fun SetupStatusBar() {

}