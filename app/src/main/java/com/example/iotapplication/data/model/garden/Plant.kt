package com.example.iotapplication.data.model.garden

data class Plant(
    val id: Int,
    val name: String,
    val description: String,
    val minTemperature: Int,
    val maxTemperature: Int,
    val minAirHumidity: Int,
    val maxAirHumidity: Int,
    val minSoilMoisture: Int,
    val maxSoilMoisture: Int
)
