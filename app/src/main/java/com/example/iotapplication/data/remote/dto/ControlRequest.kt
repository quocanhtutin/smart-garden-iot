package com.example.iotapplication.data.remote.dto

data class ControlRequest(
    val deviceId: Int,
    val state: Boolean
)