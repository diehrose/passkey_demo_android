package com.pinpin.passkey_demo.data.model

data class CredentialDeleteRequest(
    val username: String,
    val credentialId: String
)