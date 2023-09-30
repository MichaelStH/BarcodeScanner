package com.riders.barcodescannerstl


import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BarcodeScannerSTLApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d("onCreate() - ${this::class.java.simpleName} successfully initialized")
    }
}