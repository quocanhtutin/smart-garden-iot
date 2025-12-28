package com.example.iotapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(title: String, color: Color) {
    Card {
        Column(Modifier.padding(16.dp)) {
            Text(title)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color.copy(alpha = 0.2f))
            )
        }
    }
}
