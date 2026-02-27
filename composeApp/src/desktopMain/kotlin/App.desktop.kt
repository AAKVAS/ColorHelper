import androidx.compose.runtime.Composable
import com.example.Res
import com.example.image_busket
import com.example.lab
import com.example.palettes
import com.example.perspective
import org.jetbrains.compose.resources.stringResource


@Composable
actual fun getMenuTabs(): List<String> {
    return listOf(
        stringResource(Res.string.palettes),
        stringResource(Res.string.image_busket),
        stringResource(Res.string.perspective),
        stringResource(Res.string.lab),
    )
}