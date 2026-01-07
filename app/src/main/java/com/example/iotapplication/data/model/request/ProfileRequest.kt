package com.example.iotapplication.data.model.request

data class UpdateProfileRequest(
    val username: String,
    val email: String
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
