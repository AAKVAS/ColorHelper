package feature.palette.data

import feature.palette.domain.PaletteRepository
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PaletteRepositoryImpl(
    private val _paletteDao: PaletteDao,
    private val _colorDao: ColorDao,
) : PaletteRepository {
    override suspend fun getPalettes(): List<ColorPalette> {
        return _paletteDao.getPalettes().map {
            it.toModel(
                _colorDao.getColorsByPaletteUid(it.uid).toColors()
            )
        }
    }

    override suspend fun savePalette(palette: ColorPalette) {
        _paletteDao.savePalette(palette.toEntity())
        if (palette.colors.isNotEmpty()) {
            val colors = palette.colors.map { it.toColorEntity() }
            _colorDao.saveColors(colors)
        }
    }

    override suspend fun updatePalette(palette: ColorPalette) {
        _paletteDao.updatePalette(palette.toEntity())
    }

    override suspend fun deletePalette(uid: String) {
        _paletteDao.deletePalette(uid = uid)
    }

    override fun observePalettes(): Flow<List<ColorPalette>> {
        return _paletteDao.observePalettes().map { list ->
            list.map {
                it.toModel(
                    _colorDao.getColorsByPaletteUid(it.uid).toColors()
                )
            }
        }
    }

    override fun observePalette(uid: String): Flow<ColorPalette?> {
        return combine(
            _paletteDao.observePalette(uid),
            _colorDao.observeColorsByPaletteUid(uid)
        ) { paletteEntity, colorEntities ->
            paletteEntity?.toModel(colorEntities.toColors())
        }
        .distinctUntilChanged()
    }

    override suspend fun updateColor(colorModel: ColorModel) {
        _colorDao.updateColor(colorModel.toColorEntity())
    }

    override suspend fun deleteColor(colorModel: ColorModel) {
        _colorDao.deleteColorByUid(colorModel.uid)
    }

    override suspend fun addColor(colorModel: ColorModel) {
        _colorDao.saveColor(colorModel.toColorEntity())
    }
}