package com.pinpin.passkey_demo.data.model

import com.google.gson.JsonObject

data class VerifyLoginRequest(
    val userId: String,
    val response: JsonObject,
)