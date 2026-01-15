package com.example.iotapplication.data.model.garden

import com.example.iotapplication.data.model.request.UpdateGardenRequest

data class Garden(
    val id: Int,
    val gardenName: String,
    val description: String?,
    val irrigationMode: String,
    val device: Device?,
    val plant: Plant?,
    val createdAt: String
)
data class GardenDetail(
    val id: Int,
    val gardenName: String,
    val description: String?,
    val irrigationMode: String,

    val autoIrrigationThreshold: Int?,
    val autoIrrigationDuration: Int?,
    val periodicIntervalHours: Int?,

    val ledAutoMode: Boolean,

    val alertMinTemperature: Int,
    val alertMaxTemperature: Int,
    val alertMinSoilMoisture: Int,

    val device: Device?,
    val plant: Plant?
)

fun GardenDetail.toUpdateRequest(
    gardenName: String = this.gardenName,
    description: String? = this.description,
    deviceCode: String? = this.device?.deviceCode,
    plantId: Int? = this.plant?.id,
    irrigationMode: String = this.irrigationMode,
    autoIrrigationThreshold: Int? = this.autoIrrigationThreshold,
    autoIrrigationDuration: Int? = this.autoIrrigationDuration,
    periodicIntervalHours: Int? = this.periodicIntervalHours,
    ledAutoMode: Boolean = this.ledAutoMode,
    alertMinTemperature: Int = this.alertMinTemperature,
    alertMaxTemperature: Int = this.alertMaxTemperature,
    alertMinSoilMoisture: Int = this.alertMinSoilMoisture
) = UpdateGardenRequest(
    gardenName,
    description,
    deviceCode,
    plantId,
    irrigationMode,
    autoIrrigationThreshold,
    autoIrrigationDuration,
    periodicIntervalHours,
    ledAutoMode,
    alertMinTemperature,
    alertMaxTemperature,
    alertMinSoilMoisture
)

data class GardenStatusResponse(
    val gardenId: Int,
    val gardenName: String,
    val irrigationMode: String,
    val device: GardenDeviceStatus
)

data class GardenDeviceStatus(
    val deviceCode: String,
    val isConnected: Boolean,
    val isPumpOn: Boolean,
    val isLedOn: Boolean,
    val lastSeen: String?,
    val sensors: SensorData
)

data class SensorData(
    val temperature: Double?,
    val airHumidity: Double?,
    val soilMoisture: Double?,
    val isDark: Boolean
)

data class SensorLog(
    val temperature: Double?,
    val airHumidity: Double?,
    val soilMoisture: Double?,
    val recordedAt: String
)

data class SensorStatistics(
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val minSoilMoisture: Double?,
    val maxSoilMoisture: Double?
)



