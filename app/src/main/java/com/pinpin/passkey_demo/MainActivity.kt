package com.pinpin.passkey_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pinpin.passkey_demo.ui.theme.Passkey_demoTheme
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.pinpin.passkey_demo.credential.PasskeyManager
import com.pinpin.passkey_demo.data.PasskeyRepository
import com.pinpin.passkey_demo.data.model.RegisterOptionsRequest
import com.pinpin.passkey_demo.network.RetrofitClient
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val passkeyManager = PasskeyManager(this)

        val repository = PasskeyRepository(
            api = RetrofitClient.passkeyApi,
            passkeyManager = passkeyManager,
            Gson()
        )

        val viewModel = PasskeyViewModel(
            repository = repository
        )

        setContent {
            PasskeyScreen(
                onRegister = { username ->
                    viewModel.register(username)
                }
            )
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Passkey_demoTheme {
        Greeting("Android")
    }
}