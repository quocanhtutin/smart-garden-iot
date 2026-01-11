package com.example.iotapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapplication.data.model.garden.IrrigationLog
import com.example.iotapplication.data.model.garden.IrrigationStatistics
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.launch

class IrrigationHistoryViewModel(
    private val api: ApiService
) : ViewModel() {

    var statistics by mutableStateOf<IrrigationStatistics?>(null)
        private set

    var logs by mutableStateOf<List<IrrigationLog>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun load(gardenId: Int, from: String, to: String) {
        viewModelScope.launch {
            loading = true
            try {
                statistics = api.getIrrigationStatistics(gardenId, from, to).data
                logs = api.getIrrigationLogs(gardenId, from, to).data
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }
}
