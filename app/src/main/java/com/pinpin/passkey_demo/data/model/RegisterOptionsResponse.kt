package com.pinpin.passkey_demo.data.model

data class RegisterOptionsResponse(
    val challenge: String,
    val rp: Rp,
    val user: WebAuthnUser,
    val pubKeyCredParams: List<PubKeyCredParam>,
    val timeout: Long,
    val attestation: String,
    val excludeCredentials: List<ExcludeCredential>,
    val authenticatorSelection: AuthenticatorSelection?,
    val extensions: Extensions?,
    val hints: List<String>,
    val userId: Long
)

data class Rp(
    val name: String,
    val id: String
)

data class WebAuthnUser(
    val id: String,
    val name: String,
    val displayName: String
)

data class PubKeyCredParam(
    val alg: Int,
    val type: String
)

data class ExcludeCredential(
    val id: String
)

data class AuthenticatorSelection(
    val residentKey: String,
    val userVerification: String,
    val requireResidentKey: Boolean
)

data class Extensions(
    val credProps: Boolean
)