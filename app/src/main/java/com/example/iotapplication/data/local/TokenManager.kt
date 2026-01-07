package com.example.iotapplication.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = access
            it[REFRESH_TOKEN] = refresh
        }
    }

    suspend fun getAccessToken(): String? =
        context.dataStore.data.first()[ACCESS_TOKEN]

    suspend fun getRefreshToken(): String? =
        context.dataStore.data.first()[REFRESH_TOKEN]

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}