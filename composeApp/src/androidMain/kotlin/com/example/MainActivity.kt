package com.example

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import feature.home.DefaultRootComponent
import ui.composeComponents.SetupStatusBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lifecycle = LifecycleRegistry()
        val root = DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )

        setContent {
            val windowSize = with(LocalDensity.current) {
                currentWindowSize().toSize().toDpSize()
            }
            SetupStatusBar()
            App(
                rootComponent = root,
                windowSize = windowSize
            )
        }
    }
}