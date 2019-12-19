package com.example.prueba.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context

class Utils(private val application: Application) {

    fun saveStringInSp( key : String, value : String ){
        val editor = application.getSharedPreferences( "Logeo", Activity.MODE_PRIVATE).edit()

        editor.putString(key, value)
        editor.apply()
    }

    fun getStringFromSp(key : String) : String{

        val sharedPreference = application.getSharedPreferences("Logeo", Activity.MODE_PRIVATE)
        return sharedPreference.getString(key, null)!!
    }
}