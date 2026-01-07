package com.example.iotapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Gardens : BottomNavItem(
        route = "gardens",
        title = "Vườn",
        icon = Icons.Filled.Yard
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Filled.Person
    )
}

