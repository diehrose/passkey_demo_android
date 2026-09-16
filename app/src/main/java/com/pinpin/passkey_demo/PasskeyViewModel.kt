package com.pinpin.passkey_demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinpin.passkey_demo.data.PasskeyRepository
import com.pinpin.passkey_demo.data.model.Credential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasskeyViewModel(
    private val repository: PasskeyRepository
) : ViewModel() {

    companion object {
        private const val TAG = "PasskeyViewModel"
    }

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
    val isLoading =
        _isLoading.asStateFlow()

    private val _loadingMessage =
        MutableStateFlow("")
    val loadingMessage =
        _loadingMessage.asStateFlow()

    private val _message =
        MutableStateFlow<String?>(null)
    val message =
        _message.asStateFlow()


    // ============================================================
    // Passkey Management
    // ============================================================

    private val _credentials =
        MutableStateFlow<List<Credential>>(emptyList())

    val credentials =
        _credentials.asStateFlow()

    private val _isCredentialLoading =
        MutableStateFlow(false)

    val isCredentialLoading =
        _isCredentialLoading.asStateFlow()

    private val _credentialMessage =
        MutableStateFlow<String?>(null)

    val credentialMessage =
        _credentialMessage.asStateFlow()


    // ============================================================
    // Register
    // ============================================================

    fun register(username: String) {
        if (username.isBlank()) {
            _message.value = "Username 不可為空"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value =
                "正在建立 Passkey，請稍候..."
            _message.value = null
            _registerSuccess.value = false
            _loginSuccess.value = false

            repository
                .register(username)
                .onSuccess {
                    _registerSuccess.value = true
                    _message.value =
                        "Passkey 建立成功"
                }
                .onFailure { error ->
                    _message.value =
                        error.message
                            ?: "Passkey 建立失敗，請稍後再試。"
                }

            _isLoading.value = false
            _loadingMessage.value = ""
        }
    }


    // ============================================================
    // Login
    // ============================================================

    fun login(username: String) {
        if (username.isBlank()) {
            _message.value = "Username 不可為空"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value =
                "正在驗證 Passkey，請稍候..."
            _message.value = null
            _loginSuccess.value = false
            _registerSuccess.value = false

            repository
                .login(username)
                .onSuccess {
                    _loginSuccess.value = true
                    _message.value =
                        "登入成功"
                }
                .onFailure { error ->
                    _message.value =
                        "登入失敗：${
                            error.message
                                ?: "未知錯誤"
                        }"
                }

            _isLoading.value = false
            _loadingMessage.value = ""
        }
    }


    // ============================================================
    // Get Passkey Credentials
    // ============================================================

    fun loadCredentials(
        username: String
    ) {
        if (username.isBlank()) {
            _credentialMessage.value =
                "Username 不可為空"
            return
        }

        viewModelScope.launch {
            try {

                Log.d(
                    TAG,
                    "========== Load Credentials START =========="
                )

                Log.d(
                    TAG,
                    "username = $username"
                )

                _isCredentialLoading.value = true
                _credentialMessage.value = null

                val result =
                    repository.getCredentials(
                        username
                    )

                Log.d(
                    TAG,
                    "userId = ${result.userId}"
                )

                Log.d(
                    TAG,
                    "credential count = ${result.credentials.size}"
                )

                _credentials.value =
                    result.credentials

                Log.d(
                    TAG,
                    "========== Load Credentials SUCCESS =========="
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "========== Load Credentials FAILED ==========",
                    e
                )

                _credentialMessage.value =
                    e.message
                        ?: "取得 Passkey 失敗"

            } finally {

                _isCredentialLoading.value =
                    false
            }
        }
    }


    // ============================================================
    // Delete Passkey Credential
    // ============================================================

    fun deleteCredential(
        username: String,
        credentialId: String
    ) {
        if (username.isBlank()) {
            _credentialMessage.value =
                "Username 不可為空"
            return
        }

        if (credentialId.isBlank()) {
            _credentialMessage.value =
                "Credential ID 不可為空"
            return
        }

        viewModelScope.launch {
            try {

                Log.d(
                    TAG,
                    "========== Delete Credential START =========="
                )

                _isCredentialLoading.value = true
                _credentialMessage.value = null

                val result =
                    repository.deleteCredential(
                        username = username,
                        credentialId = credentialId
                    )

                if (!result.deleted) {
                    _credentialMessage.value =
                        "Passkey 刪除失敗"
                    return@launch
                }

                Log.d(
                    TAG,
                    "Credential deleted successfully"
                )

                // 重新取得清單
                val listResult =
                    repository.getCredentials(username)

                _credentials.value =
                    listResult.credentials

                _credentialMessage.value =
                    "Passkey 已刪除"

                Log.d(
                    TAG,
                    "credential count = ${listResult.credentials.size}"
                )

                Log.d(
                    TAG,
                    "========== Delete Credential SUCCESS ==========",
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "========== Delete Credential FAILED ==========",
                    e
                )

                _credentialMessage.value =
                    e.message
                        ?: "刪除 Passkey 失敗"

            } finally {

                _isCredentialLoading.value =
                    false
            }
        }
    }


    // ============================================================
    // Reset
    // ============================================================

    fun resetSuccessState() {
        _registerSuccess.value = false
        _loginSuccess.value = false
        _message.value = null
    }

    fun resetCredentialMessage() {
        _credentialMessage.value = null
    }
}