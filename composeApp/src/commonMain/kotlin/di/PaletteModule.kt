package di

import feature.palette.domain.InMemoryPaletteRepository
import feature.palette.domain.PaletteRepository
import org.koin.dsl.module

val paletteModule = module {
    single<PaletteRepository> { InMemoryPaletteRepository() }
}