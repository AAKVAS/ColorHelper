package ui.composeComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TooltipWrapper(
    modifier: Modifier = Modifier,
    tooltip: @Composable () -> Unit,
    content: @Composable () -> Unit
)

@Composable
expect fun BackHandlerWrapper(
    enabled: Boolean = true,
    callback: () -> Unit
)

@Composable
expect fun SetupStatusBar()