package com.example.iotapplication.data.repository

import android.content.Context
import com.example.iotapplication.data.local.TokenManager
import com.example.iotapplication.data.model.auth.LoginRequest
import com.example.iotapplication.data.remote.ApiClient

class AuthRepository(context: Context) {

    private val api = ApiClient.create(context)
    private val tokenManager = TokenManager(context)

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val res = api.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

            tokenManager.saveTokens(
                access = res.data.tokens.accessToken,
                refresh = res.data.tokens.refreshToken
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.clear()
    }
}
