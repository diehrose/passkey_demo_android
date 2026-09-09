package com.pinpin.passkey_demo.credential

import android.content.Context
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException

class PasskeyManager(
    private val context: Context
) {

    private val credentialManager =
        CredentialManager.Companion.create(context)

    suspend fun createPasskey(
        requestJson: String
    ): Result<CreatePublicKeyCredentialResponse> {

        return try {

            val request =
                CreatePublicKeyCredentialRequest(
                    requestJson = requestJson
                )

            val result =
                credentialManager.createCredential(
                    context = context,
                    request = request
                )

            Result.success(
                result as CreatePublicKeyCredentialResponse
            )

        } catch (e: CreateCredentialException) {

            Result.failure(e)
        }
    }
}