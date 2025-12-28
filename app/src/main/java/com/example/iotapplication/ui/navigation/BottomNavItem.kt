package com.example.iotapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Filled.Home)
    object Control : BottomNavItem("control", "Control", Icons.Filled.Build)
    object Settings : BottomNavItem("settings", "Settings", Icons.Filled.Settings)
    object History : BottomNavItem("history", "History", Icons.Filled.ShowChart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
}
