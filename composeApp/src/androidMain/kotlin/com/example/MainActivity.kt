package com.example

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.ui.platform.LocalDensity
import di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin()
        setContent {
            val windowSize = with(LocalDensity.current) {
                currentWindowSize().toSize().toDpSize()
            }
            App(windowSize)
        }
    }
}