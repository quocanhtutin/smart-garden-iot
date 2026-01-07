package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iotapplication.data.local.TokenManager
import com.example.iotapplication.data.model.auth.LoginRequest
import com.example.iotapplication.data.model.auth.RegisterRequest
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    api: ApiService,
    tokenManager: TokenManager,
    onSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val fieldModifier = Modifier.fillMaxWidth()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (isRegister) "Đăng ký" else "Đăng nhập",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(24.dp))

        if (isRegister) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = fieldModifier,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = fieldModifier,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )

        Spacer(Modifier.height(12.dp))

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            modifier = fieldModifier.height(50.dp),
            enabled = !loading,
            shape = RoundedCornerShape(12.dp),
            onClick = {
                scope.launch {
                    loading = true
                    error = null
                    try {
                        val res = if (isRegister) {
                            api.register(RegisterRequest(username, email, password))
                        } else {
                            api.login(LoginRequest(email, password))
                        }

                        val auth = res.data
                        tokenManager.saveTokens(
                            auth.tokens.accessToken,
                            auth.tokens.refreshToken
                        )
                        onSuccess()
                    } catch (e: Exception) {
                        error = e.message ?: "Sai thông tin đăng nhập"
                    } finally {
                        loading = false
                    }
                }
            }
        ) {
            Text(if (isRegister) "Đăng ký" else "Đăng nhập")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { isRegister = !isRegister }) {
            Text(
                if (isRegister)
                    "Đã có tài khoản? Đăng nhập"
                else
                    "Chưa có tài khoản? Đăng ký"
            )
        }
    }
}


