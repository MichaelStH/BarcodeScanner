package com.riders.barcodescannerstl.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    var showCamera by mutableStateOf(false)
        private set
    var dataFound by mutableStateOf("")
        private set

    fun updateShowCamera(showCamera: Boolean) {
        this.showCamera = showCamera
    }

    fun updateDataFound(newValue: String) {
        this.dataFound = newValue
    }
}