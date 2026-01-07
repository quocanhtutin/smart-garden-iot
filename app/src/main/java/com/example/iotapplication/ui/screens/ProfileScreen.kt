package com.example.iotapplication.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.ui.viewmodel.ProfileViewModel
import com.example.iotapplication.ui.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    api: ApiService,
    onLogout: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(
        factory = remember { ProfileViewModelFactory(api) }
    )

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val profile = viewModel.profile ?: return
    val fieldModifier = Modifier.fillMaxWidth()

    var editing by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(profile.username) }
    var email by remember { mutableStateOf(profile.email) }

    var showChangePassword by remember { mutableStateOf(false) }
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ===== HEADER =====
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (editing) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Tên người dùng") },
                    modifier = fieldModifier,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = fieldModifier,
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Text(profile.username, style = MaterialTheme.typography.headlineMedium)
                Text(profile.email)
            }

            Spacer(Modifier.height(8.dp))

            IconButton(onClick = {
                if (editing) {
                    viewModel.updateProfile(
                        username,
                        email,
                        onFail = {
                            username = profile.username
                            email = profile.email
                        }
                    )
                }
                editing = !editing
            }) {
                Icon(
                    if (editing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }

        viewModel.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(32.dp))

        // ===== CHANGE PASSWORD =====
        if (!showChangePassword) {
            OutlinedButton(
                onClick = { showChangePassword = true },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Thay đổi mật khẩu")
            }
        } else {
            OutlinedTextField(
                value = oldPass,
                onValueChange = { oldPass = it },
                label = { Text("Mật khẩu cũ") },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                isError = viewModel.passwordError != null
            )

            viewModel.passwordError?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newPass,
                onValueChange = { newPass = it },
                label = { Text("Mật khẩu mới") },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    showChangePassword = false
                    oldPass = ""
                    newPass = ""
                }) {
                    Text("Hủy")
                }

                Button(
                    onClick = {
                        viewModel.changePassword(oldPass, newPass) {
                            showChangePassword = false
                            oldPass = ""
                            newPass = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Thay đổi")
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // ===== LOGOUT =====
        OutlinedButton(
            onClick = onLogout,
            modifier = fieldModifier,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Đăng xuất")
        }
    }
}



