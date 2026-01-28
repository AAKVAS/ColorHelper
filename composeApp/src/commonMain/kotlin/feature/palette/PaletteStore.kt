package feature.palette

import androidx.compose.ui.graphics.Color
import com.arkivanov.mvikotlin.core.store.Store
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette

interface PaletteStore : Store<PaletteStore.Intent, PaletteStore.State, PaletteStore.Label>
{
    sealed class Intent {
        data class UpdatePalette(val colorPalette: ColorPalette): Intent()
        object DeletePalette: Intent()
        object ShowDeleteDialog: Intent()
        data class SelectHarmoniousColor(val color: String): Intent()
        data class UpdateHarmoniousColors(val colors: List<Color>): Intent()
        data class AddColor(val color: String): Intent()
        data class UpdateColor(val color: ColorModel): Intent()
        data class DeleteColor(val color: ColorModel): Intent()
        data object ObservePalette: Intent()
        data class UpdateSelectedColorUid(val uid: String?): Intent()
        data class ShowDeleteColorDialog(val uid: String): Intent()
    }

    data class State(
        val palette: ColorPalette,
        val harmoniousColors: List<String> = listOf("#FFFFFFFF", "#FF000000"),
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedColorUid: String? = null
    )

    sealed class Label {
        data class ShowMessage(val message: String) : Label()
        data class ShowDeleteColorDialog(val uid: String) : Label()
        data object ShowDeletePaletteDialog : Label()
    }

    sealed class Msg {
        data class Error(val message: String) : Msg()
        data class PaletteUpdated(val palette: ColorPalette) : Msg()
        data class UpdateHarmoniousColors(val colors: List<String>) : Msg()
        data class UpdateSelectedColorUid(val uid: String?) : Msg()
    }
}