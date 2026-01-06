package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import ui.theme.Dimens
import ui.theme.LocalColorProvider

@Composable
fun SimpleButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.buttonHeight))
            .clickable { onClick() }
            .background(LocalColorProvider.current.secondary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = LocalColorProvider.current.onPrimary,
            modifier = Modifier.padding(
                vertical = Dimens.paddingSmall,
                horizontal = Dimens.paddingMedium
            )
        )
    }
}
