package feature.colorLab

import com.arkivanov.decompose.ComponentContext
import feature.colorLab.model.ColorLabState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultColorLabComponent(
    componentContext: ComponentContext
) : ColorLabComponent, ComponentContext by componentContext {
    private val _state = MutableStateFlow(
            ColorLabState(
            sphereColor = "#FFFFFFFF",
            sphereMetallicFactor = 0.01f,
            floorColor = "#FFFFFFFF",
            lightColor = "#FFFFFFFF"
        )
    )

    override val state: StateFlow<ColorLabState>
        get() = _state

    override fun updateState(colorLabState: ColorLabState) {
        _state.value = colorLabState
    }
}