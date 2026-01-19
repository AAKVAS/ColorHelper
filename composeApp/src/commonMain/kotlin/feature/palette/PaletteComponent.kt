package feature.palette

import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PaletteComponent {
    val state: StateFlow<PaletteStore.State>
    val labels: Flow<PaletteStore.Label>

    fun selectHarmoniousColor(color: String)
    fun showDeleteDialog()
    fun updatePalette(palette: ColorPalette)
    fun addColor(color: String)
    fun updateColor(index: Int, color: String)
    fun updateSelectedColorIndex(index: Int)
    fun showDeleteColorDialog(index: Int)
    fun deleteColorByIndex(index: Int)
    fun deletePalette()
}