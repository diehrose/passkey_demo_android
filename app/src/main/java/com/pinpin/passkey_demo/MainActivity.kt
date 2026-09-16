package com.pinpin.passkey_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.pinpin.passkey_demo.credential.PasskeyManager
import com.pinpin.passkey_demo.data.PasskeyRepository
import com.pinpin.passkey_demo.network.RetrofitClient
import com.pinpin.passkey_demo.ui.theme.Passkey_demoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passkeyManager =
            PasskeyManager(this)

        val repository =
            PasskeyRepository(
                api = RetrofitClient.passkeyApi,
                passkeyManager = passkeyManager,
                Gson()
            )

        val viewModel =
            PasskeyViewModel(
                repository = repository
            )

        setContent {
            Passkey_demoTheme {

                PasskeyDemoApp(
                    viewModel = viewModel
                )
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun PasskeyDemoApp(
    viewModel: PasskeyViewModel
) {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Column {

        TabRow(
            selectedTabIndex = selectedTab
        ) {

            Tab(
                selected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
                },
                text = {
                    Text("Register / Login")
                }
            )

            Tab(
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                },
                text = {
                    Text("Management")
                }
            )
        }

        when (selectedTab) {

            0 -> {
                PasskeyScreen(
                    onRegister = { username ->
                        viewModel.register(username)
                    },
                    onLogin = { username ->
                        viewModel.login(username)
                    },
                    onResetSuccess = {
                        viewModel.resetSuccessState()
                    },
                    registerSuccess =
                        viewModel.registerSuccess,
                    loginSuccess =
                        viewModel.loginSuccess,
                    isLoading =
                        viewModel.isLoading,
                    loadingMessage =
                        viewModel.loadingMessage,
                    message =
                        viewModel.message
                )
            }

            1 -> {
                PasskeyManagementScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}