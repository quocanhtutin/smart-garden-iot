package com.example.iotapplication.ui.navigation

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
import androidx.navigation.NavHostController
import com.example.iotapplication.ui.components.AppTopBar
import com.example.iotapplication.ui.components.NotificationSheet

@Composable
fun MainScreen(onLogout: () -> Unit) {
    var showNotification by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Control,
        BottomNavItem.History,
        BottomNavItem.Settings,
        BottomNavItem.Profile
    )

    Scaffold(
        topBar = {
            AppTopBar(
                showBell = currentRoute != BottomNavItem.Profile.route,
                onBellClick = { showNotification = true }
            )
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
                onLogout = onLogout
            )

            NotificationSheet(
                visible = showNotification,
                onDismiss = { showNotification = false }
            )
        }
    }
}

@Composable
fun AppMainNav(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route
    ) {
        composable("dashboard") { DashboardScreen() }
        composable("control") { ControlScreen() }
        composable("history") { HistoryScreen() }
        composable("settings", ) { SettingsScreen() }
        composable("profile") {
            ProfileScreen(
                onLogout = onLogout
            )

        }
    }
}



