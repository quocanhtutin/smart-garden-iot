package com.example.iotapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iotapplication.data.remote.api.ApiService

class IrrigationHistoryViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IrrigationHistoryViewModel(api) as T
    }
}
