package com.example.prueba

import com.example.prueba.API.PhotosRetrofit
import com.example.prueba.models.Photos
import retrofit2.Call
import retrofit2.Response

class DataSource {
    suspend fun getPhotos() : Response<List<Photos>> {
        return PhotosRetrofit.prepararPhotosRetrofit().getPhotos()
    }

    suspend fun deletePhoto( id : Int ) : Response<Boolean>{
        return PhotosRetrofit.prepararPhotosRetrofit().deletePhoto(id)
    }

    suspend fun getPhoto(id : Int) : Response<Photos>{
        return PhotosRetrofit.prepararPhotosRetrofit().getPhoto(id)
    }
}