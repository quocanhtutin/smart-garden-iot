package com.example.iotapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationSheet(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.66f)
                .align(Alignment.TopCenter)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Thông báo", style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(16.dp))

                Text("🌡️ Nhiệt độ vượt ngưỡng")
                Text("💧 Độ ẩm đất thấp")
                Text("☀️ Ánh sáng yếu")
            }
        }
    }
}
