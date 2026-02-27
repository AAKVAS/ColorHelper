package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize

@Composable
fun rememberIsPortrait(windowSize: DpSize) = remember(windowSize) {
    derivedStateOf {
        windowSize.width < windowSize.height
    }
}