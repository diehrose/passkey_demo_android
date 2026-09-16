package com.pinpin.passkey_demo.network

import com.pinpin.passkey_demo.data.model.CredentialDeleteRequest
import com.pinpin.passkey_demo.data.model.CredentialListRequest
import com.pinpin.passkey_demo.data.model.CredentialListResponse
import com.pinpin.passkey_demo.data.model.DeleteCredentialResponse
import com.pinpin.passkey_demo.data.model.LoginOptionsRequest
import com.pinpin.passkey_demo.data.model.LoginOptionsResponse
import com.pinpin.passkey_demo.data.model.RegisterOptionsRequest
import com.pinpin.passkey_demo.data.model.RegisterOptionsResponse
import com.pinpin.passkey_demo.data.model.VerifyLoginRequest
import com.pinpin.passkey_demo.data.model.VerifyLoginResponse
import com.pinpin.passkey_demo.data.model.VerifyRegistrationRequest
import com.pinpin.passkey_demo.data.model.VerifyRegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PasskeyApi {

    @POST("passkey/register/options")
    suspend fun registerOptions(
        @Body request: RegisterOptionsRequest
    ): RegisterOptionsResponse

    @POST("passkey/register/verify")
    suspend fun verifyRegistration(
        @Body request: VerifyRegistrationRequest
    ): VerifyRegistrationResponse

    @POST("passkey/login/options")
    suspend fun loginOptions(
        @Body request: LoginOptionsRequest,
    ): LoginOptionsResponse

    @POST("passkey/login/verify")
    suspend fun verifyLogin(
        @Body request: VerifyLoginRequest,
    ): VerifyLoginResponse

    @POST("passkey/credentials/list")
    suspend fun getCredentials(
        @Body request: CredentialListRequest
    ): CredentialListResponse

    @POST("passkey/credentials/delete")
    suspend fun deleteCredential(
        @Body request: CredentialDeleteRequest
    ): DeleteCredentialResponse
}