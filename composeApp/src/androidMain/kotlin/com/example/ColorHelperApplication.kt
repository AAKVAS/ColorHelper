package com.example

import android.app.Application
import di.Koin
import org.koin.android.ext.koin.androidContext

class ColorHelperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Koin.setupKoin {
            androidContext(applicationContext)
        }
    }
}