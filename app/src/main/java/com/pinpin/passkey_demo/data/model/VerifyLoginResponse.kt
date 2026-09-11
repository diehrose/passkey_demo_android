package com.pinpin.passkey_demo.data.model

data class VerifyLoginResponse(
    val verified: Boolean,
    val userId: String?,
)