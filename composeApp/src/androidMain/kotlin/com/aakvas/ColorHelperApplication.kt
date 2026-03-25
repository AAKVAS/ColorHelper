package com.aakvas

import android.app.Application
import core.di.Koin
import org.koin.android.ext.koin.androidContext

class ColorHelperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Koin.setupKoin {
            androidContext(applicationContext)
        }
    }
}