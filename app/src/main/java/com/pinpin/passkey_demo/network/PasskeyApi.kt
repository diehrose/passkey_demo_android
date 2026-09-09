package com.pinpin.passkey_demo.network

import com.pinpin.passkey_demo.data.model.RegisterOptionsRequest
import com.pinpin.passkey_demo.data.model.RegisterOptionsResponse
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
}