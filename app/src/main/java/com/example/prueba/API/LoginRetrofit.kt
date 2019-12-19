package com.example.prueba.API

import com.example.prueba.ui.login.Login
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginRetrofit {

    const val BASE_URL = "http://petroperu.gpspetroperu.com/api/"

    fun prepareLoginRetrofit() : LoginApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(LoginApi::class.java)
    }
}