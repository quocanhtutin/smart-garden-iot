package com.example.iotapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.iotapplication.data.local.TokenManager
import com.example.iotapplication.data.remote.ApiClient
import com.example.iotapplication.ui.screens.*
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val api = remember { ApiClient.create(context) }
    val tokenManager = remember { TokenManager(context) }

    NavHost(navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                api = api,
                tokenManager = tokenManager
            ) {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable("main") {
            MainScreen(
                onLogout = {
                    scope.launch {
                        tokenManager.clear()
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

