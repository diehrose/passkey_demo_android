package com.pinpin.passkey_demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PasskeyScreen(
    onRegister: (String) -> Unit,
    onLogin: (String) -> Unit,
    loginSuccess: StateFlow<Boolean>,
) {

    var username by remember {
        mutableStateOf("test@example.com")
    }

    val isLoginSuccess by loginSuccess.collectAsState()

    if (isLoginSuccess) {

        // 登入成功後顯示
        LoginSuccessScreen()

    } else {
        // 原本登入/註冊畫面
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = {
                    Text("Username")
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = {
                    onRegister(username)
                }
            ) {
                Text("建立 Passkey")
            }

            Button(
                onClick = {
                    onLogin(username)
                },
            ) {
                Text("Login with Passkey")
            }
        }
    }
}


@Composable
private fun LoginSuccessScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "✓",
            style = MaterialTheme.typography.displayLarge,
        )

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        Text(
            text = "登入成功！",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(
            modifier = Modifier.height(8.dp),
        )

        Text(
            text = "Passkey 驗證成功",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(
            modifier = Modifier.height(24.dp),
        )

        Text(
            text = "🔐 Passwordless Login",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}