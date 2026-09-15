package com.pinpin.passkey_demo

import android.content.ContentValues.TAG
import android.util.Log
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

    private val _loginSuccess =
        MutableStateFlow(false)

    val loginSuccess =
        _loginSuccess.asStateFlow()

    private val _registerSuccess =
        MutableStateFlow(false)

    val registerSuccess =
        _registerSuccess.asStateFlow()

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
            _registerSuccess.value = false

            repository
                .register(username)
                .onSuccess {

                    Log.d(
                        TAG,
                        "========== ViewModel Register SUCCESS ==========",
                    )

                    _registerSuccess.value = true
                    _message.value = "Passkey 建立成功"
                }
                .onFailure { error ->

                    Log.e(
                        TAG,
                        "========== ViewModel Register FAILED ==========",
                        error,
                    )

                    _registerSuccess.value = false

                    _message.value =
                        "Passkey 建立失敗：${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun login(username: String) {

        if (username.isBlank()) {
            _message.value = "Username 不可為空"
            return
        }

        viewModelScope.launch {

            Log.d(
                TAG,
                "========== ViewModel Login START ==========",
            )

            _isLoading.value = true
            _message.value = null
            _loginSuccess.value = false

            repository
                .login(username)
                .onSuccess {

                    Log.d(
                        TAG,
                        "========== ViewModel Login SUCCESS ==========",
                    )

                    _loginSuccess.value = true
                    _message.value = "登入成功"
                }
                .onFailure { error ->

                    Log.e(
                        TAG,
                        "========== ViewModel Login FAILED ==========",
                        error,
                    )

                    _loginSuccess.value = false

                    _message.value =
                        "登入失敗：${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun resetSuccessState() {
        _registerSuccess.value = false
        _loginSuccess.value = false
        _message.value = null
    }
}