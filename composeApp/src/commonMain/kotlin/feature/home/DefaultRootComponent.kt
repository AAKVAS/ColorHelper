package feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import feature.colorLab.ColorLabComponent
import feature.colorLab.DefaultColorLabComponent
import feature.palette.DefaultPaletteListComponent
import feature.palette.PaletteListComponent
import feature.palette.PaletteListStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable


class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.PaletteList,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.ColorLab -> RootComponent.Child.ColorLabChild(
                colorLabComponent(componentContext)
            )

            is Config.PaletteList -> RootComponent.Child.PaletteListChild(
                paletteListComponent(componentContext)
            )
        }

    override fun navigateToColorLab() {
        navigation.pushToFront(Config.ColorLab)
    }

    override fun navigateToPaletteList() {
        navigation.pushToFront(Config.PaletteList)
    }

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

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ColorLab : Config

        @Serializable
        data object PaletteList : Config
    }
}