package com.example.iotapplication.data.remote.api

import com.example.iotapplication.data.model.auth.*
import com.example.iotapplication.data.model.garden.DeviceStatus
import com.example.iotapplication.data.model.garden.Garden
import com.example.iotapplication.data.model.garden.GardenDetail
import com.example.iotapplication.data.model.garden.GardenStatusResponse
import com.example.iotapplication.data.model.garden.IrrigationLog
import com.example.iotapplication.data.model.garden.IrrigationStatistics
import com.example.iotapplication.data.model.garden.IrrigationStatus
import com.example.iotapplication.data.model.garden.Plant
import com.example.iotapplication.data.model.garden.SensorLog
import com.example.iotapplication.data.model.garden.SensorStatistics
import com.example.iotapplication.data.model.request.ChangePasswordRequest
import com.example.iotapplication.data.model.request.CreateGardenRequest
import com.example.iotapplication.data.model.request.PumpOnRequest
import com.example.iotapplication.data.model.request.UpdateGardenRequest
import com.example.iotapplication.data.model.request.UpdateProfileRequest
import com.example.iotapplication.data.model.response.ApiMessageResponse
import com.example.iotapplication.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // ---------- AUTH ----------
    @POST("auth/login")
    suspend fun login(
        @Body req: LoginRequest
    ): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body req: RegisterRequest
    ): ApiResponse<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body req: RefreshTokenRequest
    ): TokensDto

    @GET("auth/me")
    suspend fun getMe(): UserDto

    //------ Garden------
    @GET("gardens")
    suspend fun getGardens(): ApiResponse<List<Garden>>

    @GET("plants")
    suspend fun getPlants(): ApiResponse<List<Plant>>

    @POST("gardens")
    suspend fun createGarden(
        @Body request: CreateGardenRequest
    ): ApiResponse<Garden>

    @GET("gardens/{id}")
    suspend fun getGardenDetail(
        @Path("id") id: Int
    ): ApiResponse<GardenDetail>

    @PUT("gardens/{id}")
    suspend fun updateGarden(
        @Path("id") id: Int,
        @Body request: UpdateGardenRequest
    ): ApiResponse<GardenDetail>

    @POST("gardens/{id}/pump/on")
    suspend fun pumpOn(
        @Path("id") gardenId: Int,
        @Body body: PumpOnRequest
    ): ApiMessageResponse

    @POST("gardens/{id}/pump/off")
    suspend fun pumpOff(
        @Path("id") gardenId: Int
    ): ApiMessageResponse

    //Device status
    @GET("devices/{id}/status")
    suspend fun getDeviceStatus(
        @Path("id") deviceId: Int
    ): ApiResponse<DeviceStatus>

    //LED
    @POST("gardens/{id}/led/on")
    suspend fun ledOn(@Path("id") id: Int)

    @POST("gardens/{id}/led/off")
    suspend fun ledOff(@Path("id") id: Int)

    //Status
    @GET("gardens/{id}/status")
    suspend fun getGardenStatus(
        @Path("id") id: Int
    ): ApiResponse<GardenStatusResponse>

    @GET("gardens/{id}/sensors/logs")
    suspend fun getSensorLogs(
        @Path("id") gardenId: Int,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("limit") limit: Int = 100
    ): ApiResponse<List<SensorLog>>

    @GET("gardens/{id}/sensors/statistics")
    suspend fun getSensorStatistics(
        @Path("id") gardenId: Int,
        @Query("from") from: String,
        @Query("to") to: String
    ): ApiResponse<SensorStatistics>

    @DELETE("gardens/{id}")
    suspend fun deleteGarden(
        @Path("id") id: Int
    ): retrofit2.Response<Unit>

    //Profile
    // User profile
    @GET("users/profile")
    suspend fun getProfile(): ApiResponse<UserProfile>

    @PUT("users/profile")
    suspend fun updateProfile(
        @Body req: UpdateProfileRequest
    ): ApiResponse<UserProfile>

    // Change password
    @POST("users/change-password")
    suspend fun changePassword(
        @Body req: ChangePasswordRequest
    ): ApiResponse<Unit>

    //irrigation
    @GET("gardens/{gardenId}/irrigation/status")
    suspend fun getIrrigationStatus(
        @Path("gardenId") gardenId: Int
    ): ApiResponse<IrrigationStatus>

    @GET("gardens/{gardenId}/irrigation/logs")
    suspend fun getIrrigationLogs(
        @Path("gardenId") gardenId: Int,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("limit") limit: Int = 100
    ): ApiResponse<List<IrrigationLog>>

    @GET("gardens/{gardenId}/irrigation/statistics")
    suspend fun getIrrigationStatistics(
        @Path("gardenId") gardenId: Int,
        @Query("from") from: String,
        @Query("to") to: String
    ): ApiResponse<IrrigationStatistics>
}

