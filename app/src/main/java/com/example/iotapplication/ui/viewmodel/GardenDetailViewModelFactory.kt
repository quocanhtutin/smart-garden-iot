package com.example.iotapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iotapplication.data.remote.api.ApiService

class GardenDetailViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GardenDetailViewModel(api) as T
    }
}
