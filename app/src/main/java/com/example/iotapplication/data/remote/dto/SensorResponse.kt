package com.example.iotapplication.data.remote.dto

data class SensorResponse(
    val temperature: Float,
    val humidity: Float,
    val light: Float
)