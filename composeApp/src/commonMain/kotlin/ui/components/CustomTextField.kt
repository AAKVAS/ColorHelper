package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.theme.LocalColorProvider

@Composable
fun CustomTextField(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    BasicTextField(
        value = text,
        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Normal),
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(LocalColorProvider.current.primary),
        modifier = modifier
            .width(50.dp)
            .height(24.dp)
            .border(
                width = 1.dp,
                color =
                    if (isFocused) {
                        LocalColorProvider.current.primary
                    } else {
                        LocalColorProvider.current.primaryContainer
                    },
                shape = RoundedCornerShape(4.dp)
            )
            .background(LocalColorProvider.current.onPrimary)
            .padding(4.dp)
    )
}