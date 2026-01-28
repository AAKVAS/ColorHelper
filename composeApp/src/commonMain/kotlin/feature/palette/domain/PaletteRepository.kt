package feature.palette.domain


import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow

interface PaletteRepository {
    suspend fun getPalettes(): List<ColorPalette>
    suspend fun savePalette(palette: ColorPalette)
    suspend fun updatePalette(palette: ColorPalette)
    suspend fun deletePalette(uid: String)

    fun observePalettes(): Flow<List<ColorPalette>>
    fun observePalette(uid: String): Flow<ColorPalette?>

    suspend fun addColor(colorModel: ColorModel)
    suspend fun deleteColor(colorModel: ColorModel)
    suspend fun updateColor(colorModel: ColorModel)
}