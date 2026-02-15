import androidx.compose.runtime.Composable
import com.example.Res
import com.example.lab
import com.example.palettes
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun getMenuTabs(): List<String> {
    return listOf(
        stringResource(Res.string.palettes),
        stringResource(Res.string.lab)
    )
}