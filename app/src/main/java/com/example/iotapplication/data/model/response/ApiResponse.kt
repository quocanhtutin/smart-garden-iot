package com.example.iotapplication.data.model.response

data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val timestamp: String
)

data class ApiMessageResponse(
    val success: Boolean,
    val message: String
)