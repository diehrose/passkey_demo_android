package com.pinpin.passkey_demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinpin.passkey_demo.data.PasskeyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasskeyViewModel(
    private val repository: PasskeyRepository
) : ViewModel() {

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _message =
        MutableStateFlow<String?>(null)

    val message: StateFlow<String?> =
        _message.asStateFlow()

    fun register(username: String) {

        if (username.isBlank()) {
            _message.value = "Username 不可為空"
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _message.value = null

            repository
                .register(username)
                .onSuccess {
                    _message.value =
                        "Passkey 建立成功"
                }
                .onFailure { error ->
                    _message.value =
                        "Passkey 建立失敗：${error.message}"
                }

            _isLoading.value = false
        }
    }
}