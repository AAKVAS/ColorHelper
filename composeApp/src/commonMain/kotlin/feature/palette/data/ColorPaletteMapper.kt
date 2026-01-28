package feature.palette.data

import feature.palette.data.entity.ColorEntity
import feature.palette.data.entity.ColorPaletteEntity
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette

fun ColorPaletteEntity.toModel(colors: List<ColorModel>) : ColorPalette {
    return ColorPalette(
        uid = this.uid,
        name = this.name,
        colors = colors,
        createdAt = this.createdAt
    )
}

fun ColorPalette.toEntity() : ColorPaletteEntity {
    return ColorPaletteEntity(
        uid = this.uid,
        name = this.name,
        createdAt = this.createdAt
    )
}

fun List<ColorEntity>.toColors(): List<ColorModel> {
    return this.map {
        ColorModel(
            uid = it.uid,
            paletteUid = it.paletteUid,
            value = it.value,
            createdAt = it.createdAt
        )
    }
}

fun ColorModel.toColorEntity(): ColorEntity {
    return ColorEntity(
        uid = this.uid,
        paletteUid = this.paletteUid,
        value = this.value,
        createdAt = this.createdAt
    )
}