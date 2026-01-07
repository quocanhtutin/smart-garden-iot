package com.example.iotapplication.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapplication.data.model.garden.GardenStatusResponse
import com.example.iotapplication.data.model.garden.SensorLog
import com.example.iotapplication.data.model.garden.SensorStatistics
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class GardenSensorViewModel(
    private val api: ApiService
) : ViewModel() {

    var status by mutableStateOf<GardenStatusResponse?>(null)
        private set

    var expandedSensor by mutableStateOf<String?>(null)
    var logs by mutableStateOf<List<SensorLog>>(emptyList())
    var statistics by mutableStateOf<SensorStatistics?>(null)

    private var pollingJob: Job? = null

    fun startRealtime(gardenId: Int) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                try {
                    status = api.getGardenStatus(gardenId).data
                } catch (e: Exception) {
                    // log nếu cần
                }
                delay(5000)
            }
        }
    }

    fun stopRealtime() {
        pollingJob?.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleSensor(gardenId: Int, sensor: String) {
        expandedSensor = if (expandedSensor == sensor) null else sensor
        if (expandedSensor != null) loadSensorData(gardenId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSensorData(gardenId: Int) {
        viewModelScope.launch {
            val from = OffsetDateTime.now().minusDays(7).toString()
            val to = OffsetDateTime.now().toString()

            logs = api.getSensorLogs(gardenId, from, to).data
            statistics = api.getSensorStatistics(gardenId, from, to).data
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopRealtime()
    }
}

