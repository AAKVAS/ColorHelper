package ui.composeComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.example.Res
import com.example.back_arrow
import com.example.copy_icon
import com.example.delete
import com.example.outline_delete
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.theme.Dimens
import ui.theme.Dimens.iconButtonSize
import ui.theme.Dimens.paddingSmall
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
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() }
            .background(LocalColorProvider.current.secondary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = LocalColorProvider.current.onPrimary,
            modifier = Modifier.padding(
                vertical = Dimens.paddingXXSmall,
                horizontal = Dimens.paddingMedium
            )
        )
    }
}


@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.outline_delete),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(iconButtonSize)
        )
    }
}


@Composable
fun CopyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.copy_icon),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(iconButtonSize)
        )
    }
}



@Composable
fun BackNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(iconButtonSize)
        )
    }
}