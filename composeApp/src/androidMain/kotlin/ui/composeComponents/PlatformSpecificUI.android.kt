package ui.composeComponents


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TooltipWrapper(
    modifier: Modifier,
    tooltip: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    content()
}