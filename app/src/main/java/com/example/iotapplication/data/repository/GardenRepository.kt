package com.example.iotapplication.data.repository

import com.example.iotapplication.data.model.request.CreateGardenRequest
import com.example.iotapplication.data.remote.api.GardenApi

class GardenRepository(
    private val api: GardenApi
) {

    suspend fun getGardens() = api.getGardens()

    suspend fun getPlants() = api.getPlants()

    suspend fun createGarden(request: CreateGardenRequest) =
        api.createGarden(request)
}
