package com.riders.barcodescannerstl.ui.check

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.barcodescannerstl.data.IRepository
import com.riders.barcodescannerstl.data.local.model.OrderModel
import com.riders.barcodescannerstl.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class CheckViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: IRepository
) : ViewModel() {

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

                if (it.isChecked()) {
                    Timber.e("Order has already been scanned")
                    Toast
                        .makeText(context, "Order has already been scanned", Toast.LENGTH_LONG)
                        .show()
                } else {
                    updateOrder(it)
                    repository.tagAsChecked(it._id)
                }

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