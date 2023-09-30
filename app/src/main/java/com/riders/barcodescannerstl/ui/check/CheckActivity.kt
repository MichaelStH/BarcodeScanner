package com.riders.barcodescannerstl.ui.check

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import com.riders.barcodescannerstl.ui.base.BaseComposeActivity
import com.riders.barcodescannerstl.ui.main.MainActivityViewModel
import timber.log.Timber

class CheckActivity : BaseComposeActivity() {
    private val mViewModel: MainActivityViewModel by viewModels()


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")
    }

    override fun backPressed() {
        Timber.e("override | backPressed()")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}