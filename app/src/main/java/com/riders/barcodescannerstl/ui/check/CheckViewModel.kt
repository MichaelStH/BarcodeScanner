package com.riders.barcodescannerstl.ui.check

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.barcodescannerstl.data.IRepository
import com.riders.barcodescannerstl.data.local.model.OrderModel
import com.riders.barcodescannerstl.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    var order: OrderModel? by mutableStateOf(null)
        private set

    fun updateOrder(order: OrderModel) {
        this.order = order
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun getBundle(bundle: Bundle) {
        Timber.d("getBundle()")

        val qrCode = bundle.getString(Constants.EXTRA_BARCODE_DATA)
        qrCode?.let {
            Timber.d("getBundle() | barcode extra data value: $it")
            getOrder(it)
        } ?: run {
            Timber.e("Qr Code from bundle is null")
        }
    }

    private fun getOrder(barcode: String) {
        Timber.d("getOrder() | barcode: $barcode")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            // Fetch order in database
            repository.getOrder(orderNumber = barcode)?.let {
                Timber.d("Valid order: ${it.toString()}. Display user info")
                updateOrder(it)
            } ?: run {
                // Error occurred. Order not found
                Timber.e("Invalid barcode, please retry")
            }
        }
    }

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }
}