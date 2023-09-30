package com.riders.barcodescannerstl.ui.check

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import com.riders.barcodescannerstl.ui.base.BaseComposeActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CheckActivity : BaseComposeActivity() {

    private val mViewModel: CheckViewModel by viewModels()


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

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