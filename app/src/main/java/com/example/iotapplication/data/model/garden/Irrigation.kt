package com.example.iotapplication.data.model.garden

data class IrrigationStatus(
    val gardenId: Int,
    val isActive: Boolean,
    val startTime: String?,
    val mode: String?
)

data class IrrigationLog(
    val id: Int,
    val gardenId: Int,
    val mode: String,
    val status: String,
    val startTime: String,
    val endTime: String?,
    val duration: Int?,
    val note: String?
)

data class IrrigationStatistics(
    val totalIrrigations: Int,
    val totalDuration: Int,
    val avgDuration: Int,
    val byMode: List<ModeStat>
)

data class ModeStat(
    val mode: String,
    val count: Int
)
