package com.example.iotapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.iotapplication.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
