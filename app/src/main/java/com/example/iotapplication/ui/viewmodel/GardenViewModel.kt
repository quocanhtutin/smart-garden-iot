package com.example.iotapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapplication.data.model.garden.Garden
import com.example.iotapplication.data.model.garden.Plant
import com.example.iotapplication.data.model.request.CreateGardenRequest
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.launch

class GardenViewModel(
    private val api: ApiService
) : ViewModel() {

    var gardens by mutableStateOf<List<Garden>>(emptyList())
        private set

    var plants by mutableStateOf<List<Plant>>(emptyList())
        private set

    var selectedPlant by mutableStateOf<Plant?>(null)

    fun loadGardens() {
        viewModelScope.launch {
            gardens = api.getGardens().data
        }
    }

    fun loadPlants() {
        viewModelScope.launch {
            plants = api.getPlants().data
        }
    }

    fun createGarden(
        name: String,
        description: String?,
        deviceCode: String?,
        irrigationMode: String
    ) {
        viewModelScope.launch {
            val res = api.createGarden(
                CreateGardenRequest(
                    gardenName = name,
                    description = description,
                    deviceCode = deviceCode,
                    plantId = selectedPlant?.id,
                    irrigationMode = irrigationMode
                )
            )
            gardens = gardens + res.data
        }
    }
}
