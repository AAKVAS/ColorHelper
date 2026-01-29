package feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import feature.colorLab.ColorLabComponent
import feature.colorLab.DefaultColorLabComponent
import feature.palette.DefaultPaletteListComponent
import feature.palette.PaletteListComponent
import feature.palette.PaletteListStoreFactory
import kotlinx.coroutines.Dispatchers


class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    override val children: List<RootComponent.Child> = listOf(
        RootComponent.Child.PaletteListChild(
            paletteListComponent(componentContext)
        ),
        RootComponent.Child.ColorLabChild(
            colorLabComponent(componentContext)
        )
    )

    private fun colorLabComponent(componentContext: ComponentContext): ColorLabComponent =
        DefaultColorLabComponent(
            componentContext = componentContext
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun paletteListComponent(componentContext: ComponentContext): PaletteListComponent =
        DefaultPaletteListComponent(
            componentContext = componentContext,
            storeFactory = PaletteListStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.IO
            )
        )
}