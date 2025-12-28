package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iotapplication.ui.components.InfoCard

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard("Nhiệt độ", "28 °C")
        InfoCard("Độ ẩm đất", "65 %")
        InfoCard("Ánh sáng", "70 %")
    }
}
