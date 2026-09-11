package com.pinpin.passkey_demo.credential

import android.content.Context
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException

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

    /**
     * Login with Passkey
     */
    suspend fun getPasskey(
        requestJson: String,
    ): Result<PublicKeyCredential> {
        return try {

            val option =
                GetPublicKeyCredentialOption(
                    requestJson = requestJson,
                )

            val request =
                GetCredentialRequest(
                    credentialOptions = listOf(option),
                )

            val result =
                credentialManager.getCredential(
                    context = context,
                    request = request,
                )

            val credential =
                result.credential

            if (credential !is PublicKeyCredential) {
                return Result.failure(
                    IllegalStateException(
                        "Credential is not a PublicKeyCredential",
                    ),
                )
            }

            Result.success(credential)

        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}