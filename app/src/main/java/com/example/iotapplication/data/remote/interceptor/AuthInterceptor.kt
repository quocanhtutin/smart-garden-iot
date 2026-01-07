package com.example.iotapplication.data.remote.interceptor

import android.content.Context
import com.example.iotapplication.data.local.TokenManager
import com.example.iotapplication.data.model.auth.TokensDto
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenManager.getAccessToken()
        }

        val request = if (!accessToken.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
class TokenAuthenticator(
    private val context: Context
) : Authenticator {

    private val tokenManager = TokenManager(context)

    override fun authenticate(route: Route?, response: Response): Request? {

        //Nếu đã retry rồi mà vẫn 401 → logout
        if (responseCount(response) >= 2) {
            return null
        }

        val refreshToken = runBlocking {
            tokenManager.getRefreshToken()
        } ?: return null

        return try {
            val newTokens = refreshToken(refreshToken) ?: return null

            runBlocking {
                tokenManager.saveTokens(
                    newTokens.accessToken,
                    newTokens.refreshToken
                )
            }

            response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${newTokens.accessToken}")
                .build()

        } catch (e: Exception) {
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }

    private fun refreshToken(refreshToken: String): TokensDto? {
        val client = OkHttpClient()

        val body = JSONObject()
            .put("refreshToken", refreshToken)
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://10.0.2.2:3000/api/auth/refresh")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return null

        val json = JSONObject(response.body!!.string())

        return TokensDto(
            accessToken = json.getString("accessToken"),
            refreshToken = json.getString("refreshToken"),
            expiresIn = json.getInt("expiresIn")
        )
    }
}

