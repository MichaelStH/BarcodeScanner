package com.riders.barcodescannerstl.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.riders.barcodescannerstl.core.utils.CompatibilityManager
import timber.log.Timber

abstract class BaseComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        initBackPressed()
    }

    @SuppressLint("NewApi")
    fun initBackPressed() {
        Timber.d("initBackPressed()")

        if (CompatibilityManager.isTiramisu()) {

            // Handle onBackPressed for Android 13+
            onBackInvokedDispatcher
                .registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                    Timber.e("Android 13+ onBackInvokedDispatcher | OnBackInvokedDispatcher.registerOnBackInvokedCallback()")
                    backPressed()
                }
        } else {
            onBackPressedDispatcher
                .addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            Timber.e("Android 13- onBackPressedDispatcher | OnBackPressedCallback.handleOnBackPressed() | finish()")
                            // Back is pressed... Finishing the activity
                            backPressed()
                        }
                    })
        }
    }

    /**
     * OnBackPressed method following the new recommendation for Android 33+
     *
     * Due to activity's onBackPressed deprecated method
     */
    abstract fun backPressed(): Unit
}