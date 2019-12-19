package com.example.prueba.API

import com.example.prueba.models.Photos
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {
    @GET("photos")
    suspend fun getPhotos(): Response<List<Photos>>

    @PUT("photos/{id}")
    suspend fun updatePhoto(@Path("id") id : Int,
                            @Body body : Photos)

    @DELETE("photos/{id}")
    suspend fun deletePhoto(@Path("id") id : Int) : Response<Boolean>

    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id : Int): Response<Photos>
}