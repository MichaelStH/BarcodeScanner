package com.riders.barcodescannerstl.data.local.model

sealed class MainUiState {
    data object SettingUpData : MainUiState()
    data class DataSet(val done: Boolean = true) : MainUiState()
    data class SetupDataFailed(
        val exceptionMessage: String? = null,
        val exceptionClass: Exception? = null
    ) : MainUiState()

    data object DisplayCamera : MainUiState()
    data object None : MainUiState()
}
