package core.ui.composeComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.aakvas.Res
import com.aakvas.add
import com.aakvas.add_icon
import com.aakvas.back_arrow
import com.aakvas.camera_icon
import com.aakvas.close_icon
import com.aakvas.copy
import com.aakvas.copy_icon
import com.aakvas.delete
import com.aakvas.extract_from_photo
import com.aakvas.image_busket
import com.aakvas.menu_icon
import com.aakvas.outline_delete
import com.aakvas.paste_icon
import com.aakvas.select_photo
import com.aakvas.share
import com.aakvas.show_hide
import com.aakvas.visibility_icon
import com.aakvas.visibility_off_icon
import core.ui.theme.Dimens
import core.ui.theme.LocalColorProvider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
            fontSize = Dimens.smallTextSize,
            color = LocalColorProvider.current.onPrimary,
            modifier = Modifier.padding(
                vertical = Dimens.paddingXXSmall,
                horizontal = Dimens.paddingMedium
            )
        )
    }
}


@Composable
fun RoundedAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(Dimens.roundedButtonSize)
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .background(LocalColorProvider.current.secondary),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.add_icon),
            contentDescription = stringResource(Res.string.add),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onPrimary),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}

@Composable
fun RoundedCameraButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(Dimens.roundedCameraButtonSize)
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .background(LocalColorProvider.current.secondary),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.camera_icon),
            contentDescription = stringResource(Res.string.select_photo),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onPrimary),
            modifier = Modifier.size(Dimens.iconButtonSize)
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
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.outline_delete),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}

@Composable
fun CameraIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.camera_icon),
            contentDescription = stringResource(Res.string.extract_from_photo),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}


@Composable
fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.close_icon),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.menu_icon),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
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
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.copy_icon),
            contentDescription = stringResource(Res.string.copy),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}

@Composable
fun PasteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.paste_icon),
            contentDescription = stringResource(Res.string.image_busket),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}


@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.share),
            contentDescription = stringResource(Res.string.share),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}

@Composable
fun PickPhotoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.camera_icon),
            contentDescription = stringResource(Res.string.select_photo),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.bigIconButtonSize)
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
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentSize(align = Alignment.Center)
            .clip(RoundedCornerShape(Dimens.paddingSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = stringResource(Res.string.delete),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}


@Composable
fun VisibilityButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        val icon = if (visible) {
            Res.drawable.visibility_icon
        } else {
            Res.drawable.visibility_off_icon
        }
        Image(
            painter = painterResource(icon),
            contentDescription = stringResource(Res.string.show_hide),
            alignment = Alignment.Center,
            contentScale =  ContentScale.Crop,
            colorFilter = ColorFilter.tint(color = LocalColorProvider.current.onBackground),
            modifier = Modifier.size(Dimens.iconButtonSize)
        )
    }
}