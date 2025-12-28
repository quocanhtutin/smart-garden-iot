package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var temp by remember { mutableStateOf(28f) }
    var humidity by remember { mutableStateOf(65f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Cài đặt tự động", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nhiệt độ tối đa: ${temp.toInt()} °C")
        Slider(
            value = temp,
            onValueChange = { temp = it },
            valueRange = 20f..40f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Độ ẩm đất tối thiểu: ${humidity.toInt()} %")
        Slider(
            value = humidity,
            onValueChange = { humidity = it },
            valueRange = 30f..90f
        )
    }
}
