package com.example.iotapplication.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.iotapplication.ui.screens.*
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.iotapplication.data.remote.ApiClient
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.ui.components.AppTopBar
import com.example.iotapplication.ui.components.NotificationSheet
import com.example.iotapplication.ui.viewmodel.GardenViewModel
import com.example.iotapplication.ui.viewmodel.GardenViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    var showNotification by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val api = remember { ApiClient.create(context) }
    val factory = remember { GardenViewModelFactory(api) }
    val viewModel: GardenViewModel = viewModel(factory = factory)
    val items = listOf(
        BottomNavItem.Gardens,
        BottomNavItem.Profile
    )

    val isDetail = currentRoute?.startsWith("gardens") == true

    Scaffold(
        topBar = {
            if (isDetail) {
                AppTopBar(
                    showBell = currentRoute != BottomNavItem.Profile.route,
                    onBellClick = { showNotification = true }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                tint = if (currentRoute == item.route)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            AppMainNav(
                navController = navController,
                onLogout = onLogout,
                viewModel = viewModel,
                api = api
            )

            NotificationSheet(
                visible = showNotification,
                onDismiss = { showNotification = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppMainNav(
    navController: NavHostController,
    onLogout: () -> Unit,
    viewModel: GardenViewModel,
    api: ApiService
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Gardens.route
    ){
        composable("gardens") {
            GardenListScreen(
            viewModel = viewModel,
                onGardenClick = { garden ->
                    navController.navigate("garden_detail/${garden.id}")
                }

            )
        }
        composable("profile") {
            ProfileScreen(
                api=api,
                onLogout = onLogout
            )
        }
        composable(
            route = "garden_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val gardenId = backStack.arguments!!.getInt("id")
            GardenDetailScreen(
                gardenId = gardenId,
                api = api,
                navController = navController
            )
        }


    }
}



