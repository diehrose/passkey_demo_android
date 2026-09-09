package com.pinpin.passkey_demo.data

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.pinpin.passkey_demo.credential.PasskeyManager
import com.pinpin.passkey_demo.data.model.RegisterOptionsRequest
import com.pinpin.passkey_demo.data.model.VerifyRegistrationRequest
import com.pinpin.passkey_demo.network.PasskeyApi

class PasskeyRepository(
    private val api: PasskeyApi,
    private val passkeyManager: PasskeyManager,
    private val gson: Gson
) {

    suspend fun register(
        username: String
    ): Result<Unit> {

        return try {

            // 1. 向 Server 要 WebAuthn options
            val options =
                api.registerOptions(
                    RegisterOptionsRequest(
                        username = username
                    )
                )

            // 2. 將 options 轉成 JSON
            val requestJson =
                gson.toJson(options)

            // 3. Credential Manager 建立 Passkey
            val credentialResult =
                passkeyManager.createPasskey(
                    requestJson
                ).getOrThrow()

            // 4. Credential Manager 回傳的 WebAuthn JSON
            val credentialJson =
                credentialResult.registrationResponseJson

            // 5. 轉成 JsonObject
            val response =
                JsonParser.parseString(
                    credentialJson
                ).asJsonObject

            // 6. 回 Server 驗證
            val verifyResult =
                api.verifyRegistration(
                    VerifyRegistrationRequest(
                        userId = options.userId,
                        response = response
                    )
                )

            if (!verifyResult.verified) {
                error("Passkey registration verification failed")
            }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}