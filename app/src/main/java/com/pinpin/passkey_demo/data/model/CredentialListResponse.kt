package com.pinpin.passkey_demo.data.model

data class CredentialListResponse(
    val userId: Long,
    val username: String,
    val credentials: List<Credential>
)