package com.riders.barcodescannerstl.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.barcodescannerstl.data.IRepository
import com.riders.barcodescannerstl.data.local.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
/*
 * To do that we will write an extension Composable function for ViewModel which
 * will receive Composable lifecycle Owner LocalLifecycleOwner.current.lifecycle
 * and will add observer and remove observer on onDispose block.
 *
 * The ViewModel will implement DefaultLifecycleObserver and will start receiving lifecycle events.
 */
@Composable
fun <viewModel : LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
    }
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: IRepository
) : ViewModel(), DefaultLifecycleObserver {

    //////////////////////////////////////////
    // Composable
    //////////////////////////////////////////
    private var _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.None)
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    var showCamera by mutableStateOf(false)
        private set
    var dataFound by mutableStateOf("")
        private set

    var message by mutableStateOf("")
        private set

    fun updateUiState(state: MainUiState) {
        this._mainUiState.value = state
    }

    fun updateShowCamera(showCamera: Boolean) {
        this.showCamera = showCamera
    }

    fun updateDataFound(newValue: String) {
        this.dataFound = newValue
    }

    fun updateMessage(message: String) {
        this.message = message
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")

            updateUiState(MainUiState.SetupDataFailed(throwable.message, null))
            updateMessage("CoroutineExceptionHandler | ${throwable.message}")
        }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")

        updateUiState(MainUiState.SettingUpData)
        updateMessage("Setting up... Please wait")

        viewModelScope.launch(IO + SupervisorJob() + coroutineExceptionHandler) {
            delay(500L)
            repository.getOrderFromAssetsToJson(context = context)?.let {
                if (it.isEmpty()) {
                    updateUiState(MainUiState.SetupDataFailed(exceptionMessage = "List is empty"))
                    updateMessage("Error while setting up data")
                } else {
                    updateUiState(MainUiState.DataSet())
                    updateMessage("We're all set. Show camera")
                }
            } ?: run {
                Timber.e("Error while fetching data from assets ? | Returned null")
            }
        }
    }
}