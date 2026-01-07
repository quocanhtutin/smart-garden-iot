package com.example.iotapplication.data.model.auth

data class AuthResponse(
    val user: com.example.iotapplication.data.remote.dto.UserDto,
    val tokens: TokensDto
)

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val role: String
)

data class TokensDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val role: Role
)

data class Role(
    val id: Int,
    val roleName: String
)
