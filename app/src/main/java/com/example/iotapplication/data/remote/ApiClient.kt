package com.example.iotapplication.data.remote

import android.content.Context
import com.example.iotapplication.data.local.TokenManager
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.data.remote.interceptor.AuthInterceptor
import com.example.iotapplication.data.remote.interceptor.TokenAuthenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    fun create(context: Context): ApiService {

        val tokenManager = TokenManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .authenticator(TokenAuthenticator(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
