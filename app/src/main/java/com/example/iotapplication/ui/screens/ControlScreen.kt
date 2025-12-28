package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ControlScreen() {
    var isAutoMode by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Điều khiển", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Auto / Manual switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (isAutoMode) "Chế độ: AUTO" else "Chế độ: MANUAL")
            Switch(
                checked = isAutoMode,
                onCheckedChange = { isAutoMode = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isAutoMode) {
            AutoModeUI()
        } else {
            ManualModeUI()
        }
    }
}

@Composable
fun AutoModeUI() {
    Text("Hệ thống đang tự động điều khiển", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))

    StatusItem("Bơm nước", "Tự động")
    StatusItem("Quạt", "Tự động")
    StatusItem("Đèn", "Tự động")
}

@Composable
fun ManualModeUI() {
    ControlItem("Bơm nước")
    ControlItem("Quạt")
    ControlItem("Đèn")
}

@Composable
fun ControlItem(name: String) {
    var isOn by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name)
        Switch(
            checked = isOn,
            onCheckedChange = { isOn = it }
        )
    }
}

@Composable
fun StatusItem(name: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Text(status)
    }
}
