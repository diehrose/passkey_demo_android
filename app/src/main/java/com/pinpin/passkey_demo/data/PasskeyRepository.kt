package com.pinpin.passkey_demo.data

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.pinpin.passkey_demo.credential.PasskeyManager
import com.pinpin.passkey_demo.data.model.RegisterOptionsRequest
import com.pinpin.passkey_demo.data.model.VerifyRegistrationRequest
import com.pinpin.passkey_demo.network.PasskeyApi
import android.util.Log
import com.pinpin.passkey_demo.data.model.LoginOptionsRequest
import com.pinpin.passkey_demo.data.model.VerifyLoginRequest


class PasskeyRepository(
    private val api: PasskeyApi,
    private val passkeyManager: PasskeyManager,
    private val gson: Gson
) {

    companion object {
        private const val TAG = "PasskeyRepository"
    }

    suspend fun register(
        username: String
    ): Result<Unit> {

        return try {

            Log.d(TAG, "========== Passkey Registration START ==========")
            Log.d(TAG, "username = $username")

            // 1. 向 Server 要 WebAuthn options
            Log.d(TAG, "[1] Request registration options")

            val options =
                api.registerOptions(
                    RegisterOptionsRequest(
                        username = username
                    )
                )

            Log.d(TAG, "[1] Registration options received")
            Log.d(TAG, "userId = ${options.userId}")
            Log.d(TAG, "challenge = ${options.challenge}")

            // 2. 將 options 轉成 JSON
            val requestJson =
                gson.toJson(options)

            Log.d(TAG, "[2] Options JSON:")
            Log.d(TAG, requestJson)

            // 3. Credential Manager 建立 Passkey
            Log.d(TAG, "[3] Calling Credential Manager")

            val credentialResult =
                passkeyManager.createPasskey(
                    requestJson
                ).getOrThrow()

            Log.d(TAG, "[3] Passkey created successfully")

            // 4. Credential Manager 回傳的 WebAuthn JSON
            val credentialJson =
                credentialResult.registrationResponseJson

            Log.d(TAG, "[4] Registration response received")
            Log.d(TAG, "response JSON length = ${credentialJson.length}")
            Log.d(TAG, "response JSON = $credentialJson")

            // 5. 轉成 JsonObject
            val response =
                JsonParser.parseString(
                    credentialJson
                ).asJsonObject

            Log.d(TAG, "[5] Parsed response JsonObject")
            Log.d(TAG, "response keys = ${response.keySet()}")

            // 6. 回 Server 驗證
            Log.d(TAG, "[6] Sending registration response to Server")
            Log.d(TAG, "userId = ${options.userId}")

            val verifyResult =
                api.verifyRegistration(
                    VerifyRegistrationRequest(
                        userId = options.userId,
                        response = response
                    )
                )

            Log.d(TAG, "[6] Server verification result")
            Log.d(TAG, "verified = ${verifyResult.verified}")

            if (!verifyResult.verified) {
                Log.e(TAG, "Passkey registration verification FAILED")
                error("Passkey registration verification failed")
            }

            Log.d(TAG, "========== Passkey Registration SUCCESS ==========")

            Result.success(Unit)

        } catch (e: Exception) {

            Log.e(
                TAG,
                "========== Passkey Registration FAILED ==========",
                e
            )

            Result.failure(e)
        }
    }

    suspend fun login(
        username: String,
    ): Result<Unit> {
        return try {

            Log.d(
                TAG,
                "========== Passkey Login START ==========",
            )

            Log.d(
                TAG,
                "username = $username",
            )

            // ------------------------------------------------
            // [1] Get Login Options
            // ------------------------------------------------

            Log.d(
                TAG,
                "[1] Request login options",
            )

            val options =
                api.loginOptions(
                    LoginOptionsRequest(
                        username = username,
                    ),
                )

            Log.d(
                TAG,
                "[1] Login options received",
            )

            Log.d(
                TAG,
                "userId = ${options.userId}",
            )

            Log.d(
                TAG,
                "challenge = ${options.challenge}",
            )

            // ------------------------------------------------
            // [2] Ask Credential Manager
            // ------------------------------------------------

            Log.d(
                TAG,
                "[2] Calling Credential Manager",
            )

            val credential =
                passkeyManager
                    .getPasskey(
                        gson.toJson(options),
                    )
                    .getOrThrow()

            Log.d(
                TAG,
                "[2] Passkey retrieved successfully",
            )

            // ------------------------------------------------
            // [3] Get WebAuthn authentication response
            // ------------------------------------------------

            val authenticationJson =
                credential.authenticationResponseJson

            Log.d(
                TAG,
                "[3] Authentication response received",
            )

            Log.d(
                TAG,
                "response JSON length = ${authenticationJson.length}",
            )

            Log.d(
                TAG,
                "response JSON = $authenticationJson",
            )

            val response =
                JsonParser
                    .parseString(
                        authenticationJson,
                    )
                    .asJsonObject

            Log.d(
                TAG,
                "[3] Authentication response parsed",
            )

            Log.d(
                TAG,
                "response keys = ${response.keySet()}",
            )

            // ------------------------------------------------
            // [4] Send response to backend
            // ------------------------------------------------

            Log.d(
                TAG,
                "[4] Sending login response to Server",
            )

            val verifyResult =
                api.verifyLogin(
                    VerifyLoginRequest(
                        userId = options.userId,
                        response = response,
                    ),
                )

            Log.d(
                TAG,
                "[4] Server verification result",
            )

            Log.d(
                TAG,
                "verified = ${verifyResult.verified}",
            )

            if (!verifyResult.verified) {
                Log.e(
                    TAG,
                    "Passkey login verification FAILED",
                )

                error(
                    "Passkey login verification failed",
                )
            }

            Log.d(
                TAG,
                "========== Passkey Login SUCCESS ==========",
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Log.e(
                TAG,
                "========== Passkey Login FAILED ==========",
                e,
            )

            Result.failure(e)
        }
    }
}