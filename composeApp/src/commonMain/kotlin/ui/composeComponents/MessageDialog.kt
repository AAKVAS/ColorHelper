package ui.composeComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.Res
import com.example.cancel
import com.example.delete
import com.example.delete_color
import com.example.delete_palette
import com.example.not_canceled_action
import org.jetbrains.compose.resources.stringResource
import ui.theme.LocalColorProvider

@Composable
fun MessageDialog(text: String) {

}

@Composable
fun DeleteColorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        itemName = itemName,
        header = stringResource(Res.string.delete_color, itemName)
    )
}


@Composable
fun DeletePaletteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        itemName = itemName,
        header = stringResource(Res.string.delete_palette, itemName)
    )
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    header: String,
    itemName: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = LocalColorProvider.current.primaryContainer,
        title = {
            Text(
                text = header,
                fontWeight = FontWeight.Bold,
                color = LocalColorProvider.current.onSurface
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.not_canceled_action),
                    color = LocalColorProvider.current.onBackground
                )
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onConfirm,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = LocalColorProvider.current.onSurface,
                    backgroundColor = LocalColorProvider.current.primaryContainer
                ),
                border = BorderStroke(1.dp, LocalColorProvider.current.onSurface)
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = LocalColorProvider.current.onSurface
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = LocalColorProvider.current.onBackground,
                    backgroundColor = LocalColorProvider.current.primaryContainer
                ),
                border = BorderStroke(1.dp, LocalColorProvider.current.onBackground)
            ) {
                Text(
                    text = stringResource(Res.string.cancel),
                    color = LocalColorProvider.current.onBackground
                )
            }
        }
    )
}
