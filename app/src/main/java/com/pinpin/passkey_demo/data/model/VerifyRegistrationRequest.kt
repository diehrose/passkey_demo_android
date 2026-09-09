package com.pinpin.passkey_demo.data.model

import com.google.gson.JsonObject

data class VerifyRegistrationRequest(
    val userId: Long,
    val response: JsonObject
)