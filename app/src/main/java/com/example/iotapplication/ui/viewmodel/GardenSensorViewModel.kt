package com.example.iotapplication.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@RequiresApi(Build.VERSION_CODES.O)
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

//            val apiLogs = api.getSensorLogs(gardenId, from, to).data
//
//            logs = if (apiLogs.isNullOrEmpty()) {
//                fakeSensorLogs()
//            } else {
//                apiLogs
//            }
            statistics = api.getSensorStatistics(gardenId, from, to).data
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopRealtime()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun fakeSensorLogs(): List<SensorLog> {
    val now = OffsetDateTime.now()

    return (0..6).flatMap { dayOffset ->
        val date = now.minusDays(dayOffset.toLong())

        // mỗi ngày 6 log (sáng → tối)
        listOf(
            SensorLog(25.0 + dayOffset, 60.0, 40.0, date.withHour(6).toString()),
            SensorLog(26.5 + dayOffset, 62.0, 42.0, date.withHour(9).toString()),
            SensorLog(28.0 + dayOffset, 65.0, 45.0, date.withHour(12).toString()),
            SensorLog(29.5 + dayOffset, 68.0, 48.0, date.withHour(15).toString()),
            SensorLog(27.0 + dayOffset, 64.0, 43.0, date.withHour(18).toString()),
            SensorLog(26.0 + dayOffset, 61.0, 41.0, date.withHour(21).toString())
        )
    }
}


