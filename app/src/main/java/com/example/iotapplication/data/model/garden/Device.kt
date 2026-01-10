package com.example.iotapplication.data.model.garden

data class Device (
    val id: Int,
    val deviceCode: String,
    val temperature: Double?,
    val airHumidity: Int?,
    val soilMoisture: Int?,
    val isDark: Boolean,
    val isLedOn: Boolean,
    val isConnected: Boolean,
    val lastSeen: String
)

data class DeviceStatus(
    val deviceId: Int,
    val deviceCode: String,
    val isOnline: Boolean,
    val lastSeen: String?
)

