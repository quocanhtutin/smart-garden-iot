package com.example.iotapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    showBell: Boolean,
    onBellClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Smart Garden") },
        actions = {
            if (showBell) {
                IconButton(onClick = onBellClick) {
                    Icon(Icons.Filled.Notifications, contentDescription = null)
                }
            }
        }
    )
}
