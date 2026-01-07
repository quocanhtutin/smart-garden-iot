package com.example.iotapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iotapplication.data.remote.api.ApiService

class GardenViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GardenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GardenViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
