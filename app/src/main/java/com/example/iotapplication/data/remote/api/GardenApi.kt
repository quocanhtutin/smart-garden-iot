package com.example.iotapplication.data.remote.api

import com.example.iotapplication.data.model.garden.*
import com.example.iotapplication.data.model.request.CreateGardenRequest
import com.example.iotapplication.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GardenApi {

    @GET("/api/gardens")
    suspend fun getGardens(): ApiResponse<List<Garden>>

    @GET("/api/plants")
    suspend fun getPlants(): ApiResponse<List<Plant>>

    @POST("/api/gardens")
    suspend fun createGarden(
        @Body request: CreateGardenRequest
    ): ApiResponse<Garden>
}
