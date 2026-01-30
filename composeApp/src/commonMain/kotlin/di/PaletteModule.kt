package di

import data.AppDatabase
import feature.palette.data.ColorDao
import feature.palette.data.PaletteDao
import feature.palette.data.PaletteRepositoryImpl
import feature.palette.domain.ColorCompositor
import feature.palette.domain.PaletteRepository
import org.koin.dsl.module

val paletteModule = module {
    single<PaletteDao> { get<AppDatabase>().getPaletteDao() }
    single<ColorDao> { get<AppDatabase>().getColorDao() }
    single<PaletteRepository> { PaletteRepositoryImpl(get(), get()) }
    single<ColorCompositor> { ColorCompositor() }
}