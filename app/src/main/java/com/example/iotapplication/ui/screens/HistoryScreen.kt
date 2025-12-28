package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iotapplication.ui.components.LineChart
import com.example.iotapplication.ui.theme.HumidityColor
import com.example.iotapplication.ui.theme.LightColor
import com.example.iotapplication.ui.theme.TemperatureColor

@Composable
fun HistoryScreen() {
    Column(Modifier.padding(16.dp)) {
        LineChart("Nhiệt độ", TemperatureColor)
        Spacer(Modifier.height(16.dp))
        LineChart("Độ ẩm", HumidityColor)
        Spacer(Modifier.height(16.dp))
        LineChart("Ánh sáng", LightColor)
    }
}


@Composable
fun HistoryItem(device: String, action: String, time: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(device)
            Text("$action • $time")
        }
    }
}
