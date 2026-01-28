package ui.composeComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
fun DeleteColorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
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
        header = stringResource(Res.string.delete_palette, itemName)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    header: String,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LocalColorProvider.current.primaryContainer,
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
                colors = outlinedButtonColors(
                    contentColor = LocalColorProvider.current.onSurface,
                    containerColor = LocalColorProvider.current.primaryContainer
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
                colors = outlinedButtonColors(
                    contentColor = LocalColorProvider.current.onBackground,
                    containerColor = LocalColorProvider.current.primaryContainer
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
