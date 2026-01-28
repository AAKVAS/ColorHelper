package feature.colorLab

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import feature.colorLab.model.ColorLabState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultColorLabComponent(
    componentContext: ComponentContext
) : ColorLabComponent, ComponentContext by componentContext {
    private val _stateInstance = instanceKeeper.getOrCreate {
        StateInstance(
            MutableStateFlow(
                ColorLabState(
                    sphereColor = "#FFFFFFFF",
                    sphereMetallicFactor = 0.01f,
                    floorColor = "#FFFFFFFF",
                    lightColor = "#FFFFFFFF"
                )
            )
        )
    }


    override val state: StateFlow<ColorLabState>
        get() = _stateInstance.state

    override fun updateState(colorLabState: ColorLabState) {
        _stateInstance.state.value = colorLabState
    }

    private data class StateInstance(
        val state: MutableStateFlow<ColorLabState>
    ) : InstanceKeeper.Instance
}