package com.pinpin.passkey_demo.data.model

data class LoginOptionsResponse(
    val rpId: String,
    val challenge: String,
    val allowCredentials: List<AllowCredential>,
    val timeout: Long,
    val userVerification: String,
    val userId: String,
)
