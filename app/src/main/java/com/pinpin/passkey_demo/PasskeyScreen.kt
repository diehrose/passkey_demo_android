package com.pinpin.passkey_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun PasskeyScreen(
    onRegister: (String) -> Unit,
    onLogin: (String) -> Unit,
    onResetSuccess: () -> Unit,
    registerSuccess: StateFlow<Boolean>,
    loginSuccess: StateFlow<Boolean>,
    isLoading: StateFlow<Boolean>,
    loadingMessage: StateFlow<String>,
    message: StateFlow<String?>,
) {
    var username by remember {
        mutableStateOf("test@example.com")
    }

    val isRegisterSuccess by registerSuccess.collectAsState()
    val isLoginSuccess by loginSuccess.collectAsState()
    val loading by isLoading.collectAsState()
    val progressMessage by loadingMessage.collectAsState()
    val errorMessage by message.collectAsState()

    when {
        loading -> {
            PasskeyLoadingScreen(
                message = progressMessage,
            )
        }

        isRegisterSuccess -> {
            RegisterSuccessScreen(
                onContinue = onResetSuccess,
            )
        }

        isLoginSuccess -> {
            LoginSuccessScreen()
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    label = {
                        Text("Username")
                    },
                    singleLine = true,
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        onRegister(username)
                    },
                    enabled = !loading,
                ) {
                    Text("建立 Passkey")
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {
                        onLogin(username)
                    },
                    enabled = !loading,
                ) {
                    Text("Login with Passkey")
                }

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Text(
                        text = errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterSuccessScreen(
    onContinue: () -> Unit
) {

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
            text = "Passkey 建立成功！",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(
            modifier = Modifier.height(8.dp),
        )

        Text(
            text = "這台裝置已建立 Passkey",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(
            modifier = Modifier.height(24.dp),
        )

        Button(
            onClick = onContinue,
        ) {
            Text("繼續登入")
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

@Composable
private fun PasskeyLoadingScreen(
    message: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "請依照系統提示完成操作",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}