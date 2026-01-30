package ui.composeComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import ui.theme.Dimens
import ui.theme.LocalColorProvider

@Composable
fun CustomTextField(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    onValueChange: (String) -> Unit
) {
    val internalText = remember { mutableStateOf(text) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val focusManager = LocalFocusManager.current

    LaunchedEffect(text) {
        internalText.value = text
    }

    LaunchedEffect(isFocused) {
        if (!isFocused) {
            if (internalText.value != text) {
                onValueChange(internalText.value)
            }
        }
    }

    BasicTextField(
        value = internalText.value,
        textStyle = TextStyle(
            color = LocalColorProvider.current.onBackground,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize
        ),
        onValueChange = { newText ->
            internalText.value = newText
        },
        interactionSource = interactionSource,
        cursorBrush = SolidColor(LocalColorProvider.current.primary),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        modifier = modifier
            .onPreviewKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.Enter, Key.NumPadEnter, Key.Escape -> {
                        focusManager.clearFocus()
                        true
                    }
                    else -> false
                }
            }
            .border(
                width = Dimens.smallestPadding,
                color =
                    if (isFocused) {
                        LocalColorProvider.current.primary
                    } else {
                        LocalColorProvider.current.primaryContainer
                    },
                shape = RoundedCornerShape(Dimens.roundedCornerShapeSize)
            )
            .background(LocalColorProvider.current.onPrimary)
            .padding(Dimens.paddingXSmall)
    )
}