package com.example.iotapplication.data.model.request

data class UpdateGardenRequest(
    val gardenName: String,
    val description: String?,
    val deviceCode: String?,
    val plantId: Int?,
    val irrigationMode: String,
    val autoIrrigationThreshold: Int?,
    val autoIrrigationDuration: Int?,
    val periodicIntervalHours: Int?,
    val ledAutoMode: Boolean,
    val alertMinTemperature: Int,
    val alertMaxTemperature: Int,
    val alertMinSoilMoisture: Int
)
