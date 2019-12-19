package com.example.prueba.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PhotosRetrofit {

    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun prepararPhotosRetrofit(): PhotosApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PhotosApi::class.java)
    }
}