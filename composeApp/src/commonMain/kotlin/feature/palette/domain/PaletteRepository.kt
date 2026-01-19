package feature.palette.domain


import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow

interface PaletteRepository {
    suspend fun getPalettes(): List<ColorPalette>
    suspend fun getPaletteById(id: String): ColorPalette?
    suspend fun savePalette(palette: ColorPalette)
    suspend fun deletePalette(id: String)
    suspend fun updatePalette(palette: ColorPalette)

    fun observePalettes(): Flow<List<ColorPalette>>
    fun observePalette(id: String): Flow<ColorPalette?>
}