package com.example.iotapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapplication.data.model.garden.GardenDetail
import com.example.iotapplication.data.model.request.PumpOnRequest
import com.example.iotapplication.data.model.request.UpdateGardenRequest
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.iotapplication.data.model.garden.DeviceStatus


class GardenDetailViewModel(
    private val api: ApiService
) : ViewModel() {

    var gardenDetail by mutableStateOf<GardenDetail?>(null)
        private set

    var isLoading by mutableStateOf(false)

    var pumpError by mutableStateOf<String?>(null)
        private set

    var deviceStatus by mutableStateOf<DeviceStatus?>(null)
        private set

    var ledError by mutableStateOf<String?>(null)
        private set

    fun loadGarden(id: Int) {
        viewModelScope.launch {
            isLoading = true
            gardenDetail = api.getGardenDetail(id).data
            gardenDetail?.device?.id?.let {
                loadDeviceStatus(it)
            }
            isLoading = false
        }
    }

    fun updateGarden(request: UpdateGardenRequest) {
        val id = gardenDetail?.id ?: return
        viewModelScope.launch {
            val res = api.updateGarden(id, request)
            gardenDetail = res.data
        }
    }

    fun pumpOn(durationSeconds: Int) {
        val id = gardenDetail?.id ?: return

        viewModelScope.launch {
            pumpError = null
            try {
                api.pumpOn(id, PumpOnRequest(durationSeconds))
            } catch (e: HttpException) {
                pumpError = when (e.code()) {
                    400 -> "Thiết bị offline hoặc chưa được gán"
                    409 -> "Máy bơm đang chạy"
                    else -> "Lỗi không xác định"
                }
            }
        }
    }

    fun pumpOff() {
        val id = gardenDetail?.id ?: return

        viewModelScope.launch {
            pumpError = null
            try {
                api.pumpOff(id)
            } catch (e: Exception) {
                pumpError = "Không thể tắt máy bơm"
            }
        }
    }

    fun loadDeviceStatus(deviceId: Int) {
        viewModelScope.launch {
            try {
                val res = api.getDeviceStatus(deviceId)
                deviceStatus = res.data
            } catch (e: Exception) {
                deviceStatus = null
            }
        }
    }

    fun ledOn(gardenId: Int) {
        viewModelScope.launch {
            try {
                api.ledOn(gardenId)
                ledError = null
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    ledError = "Thiết bị đang offline"
                }
            }
        }
    }

    fun ledOff(gardenId: Int) {
        viewModelScope.launch {
            api.ledOff(gardenId)
            ledError = null
        }
    }

    fun deleteGarden(
        gardenId: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                api.deleteGarden(gardenId)
                onSuccess()
            } catch (e: HttpException) {
                onError()
            }
        }
    }

}
