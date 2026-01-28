package feature.palette

import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PaletteComponent {
    val state: StateFlow<PaletteStore.State>
    val labels: Flow<PaletteStore.Label>

    fun selectHarmoniousColor(color: String)
    fun showDeleteDialog()
    fun addColor(color: String)
    fun updateColor(color: ColorModel)
    fun deleteColor(color: ColorModel)
    fun updateSelectedColorUid(uid: String)
    fun showDeleteColorDialog(uid: String)
    fun deletePalette()
    fun updatePalette(colorPalette: ColorPalette)
    fun navigateBack()
}