package com.example.iotapplication.data.remote.dto

data class LoginResponse(
    val accessToken: String,
    val user: com.example.iotapplication.data.model.auth.UserDto
)

data class UserDto(
    val id: Int,
    val email: String
)
