package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Nguyễn Văn A", style = MaterialTheme.typography.headlineMedium)
        Text("Email: user@gmail.com")

        Spacer(Modifier.height(24.dp))

        Button(onClick = { /* connect device */ }) {
            Text("Kết nối thiết bị")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(onClick = onLogout) {
            Text("Đăng xuất")
        }
    }
}
