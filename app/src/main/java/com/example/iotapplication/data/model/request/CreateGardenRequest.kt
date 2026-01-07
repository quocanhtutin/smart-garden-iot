package com.example.iotapplication.data.model.request

data class CreateGardenRequest(
    val gardenName: String,
    val description: String?,
    val deviceCode: String?,
    val plantId: Int?,
    val irrigationMode: String
)
