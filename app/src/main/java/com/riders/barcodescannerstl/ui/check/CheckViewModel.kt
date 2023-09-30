package com.riders.barcodescannerstl.ui.check

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.barcodescannerstl.data.IRepository
import com.riders.barcodescannerstl.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    fun getBundle(bundle: Bundle) {
        Timber.d("getBundle()")

        val qrCode = bundle.getString(Constants.EXTRA_BARCODE_DATA)
        qrCode?.let { getOrder(it) } ?: run { Timber.e("Qr Code from bundle is null") }
    }

    fun getOrder(barcode: String) {
        Timber.d("getOrder() | barcode: $barcode")
        viewModelScope.launch {
            val order = repository.getOrder(orderNumber = barcode)
            Timber.d("order: ${order.toString()}")
        }
    }
}