package com.riders.barcodescannerstl


import com.riders.barcodescannerstl.BuildConfig
import android.app.Application
import timber.log.Timber

class BarcodeScannerSTLApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d("onCreate() - ${this::class.java.simpleName} successfully initialized")
    }
}