package com.example.prueba.ModelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prueba.models.Photos

class PhotosViewModel : ViewModel(){

    private val input = MutableLiveData<List<Photos>>()

    fun setInput(listaPhotos : List<Photos>){
        input.value = listaPhotos
    }

    fun getInput() : LiveData<List<Photos>>{
        return input
    }
}