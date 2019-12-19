package com.example.prueba.API

import com.example.prueba.data.model.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @FormUrlEncoded
    @Headers(
        "Content-Type: application/json",
        "COntent-Type: application/x-www-form-urlencoded"
    )

    @POST("authentication/login/")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Token>
}