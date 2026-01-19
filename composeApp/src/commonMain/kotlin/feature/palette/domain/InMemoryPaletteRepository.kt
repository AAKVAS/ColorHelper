package feature.palette.domain

import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class InMemoryPaletteRepository : PaletteRepository {
    private val _palettes = MutableStateFlow(
        listOf(
            ColorPalette(
                id = UUID.randomUUID().toString(),
                name = "Пастельные тона",
                colors = listOf(
                    "#FFFFB6C1",
                    "#FFFFD700",
                )
            ),
            ColorPalette(
                id = UUID.randomUUID().toString(),
                name = "Темная тема",
                colors = listOf(
                    "#FF121212",
                    "#FF1E1E1E",
                    "#FFBB86FC"
                )
            )
        )
    )

    override suspend fun getPalettes(): List<ColorPalette> {
        return _palettes.value
    }

    override suspend fun getPaletteById(id: String): ColorPalette? =
        _palettes.value.find { it.id == id }

    override suspend fun savePalette(palette: ColorPalette) {
        _palettes.update { it + palette }
    }

    override suspend fun deletePalette(id: String) {
        _palettes.update { it.filter { palette -> palette.id != id } }
    }

    override suspend fun updatePalette(palette: ColorPalette) {
        _palettes.update { list ->
            list.map { if (it.id == palette.id) palette else it }
        }
    }

    override fun observePalettes(): Flow<List<ColorPalette>> =
        _palettes.asStateFlow()

    override fun observePalette(id: String): Flow<ColorPalette?> =
        _palettes.asStateFlow()
            .map { palettes ->
                palettes.find { it.id == id }
            }
            .distinctUntilChanged()
}