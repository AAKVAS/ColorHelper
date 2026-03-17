package core.di

import feature.perspectiveBuilder.domain.PerspectiveSceneExtractor
import feature.perspectiveBuilder.domain.PerspectiveSceneExtractorImpl
import org.koin.dsl.module


val perspectiveBuilderModule = module {
    single<PerspectiveSceneExtractor> { PerspectiveSceneExtractorImpl() }
}