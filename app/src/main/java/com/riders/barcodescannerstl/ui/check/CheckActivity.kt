package com.riders.barcodescannerstl.ui.check

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.ui.base.BaseComposeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CheckActivity : BaseComposeActivity() {

    private val mViewModel: CheckViewModel by viewModels()


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    BarcodeScannerSTLTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            CheckContent(mViewModel)
                        }
                    }
                }
            }
        }

        this.intent.extras?.let { mViewModel.getBundle(it) }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun backPressed() {
        Timber.e("override | backPressed()")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}