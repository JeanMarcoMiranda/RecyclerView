package com.example.prueba.data

import com.example.prueba.API.LoginRetrofit
import com.example.prueba.data.model.LoggedInUser
import com.example.prueba.data.model.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Response<Token> {
        return getToken(username, password)
    }

    fun logout() {
        // TODO: revoke authentication
    }

    suspend fun getToken(username : String, password: String) : Response<Token> {
        return LoginRetrofit.prepareLoginRetrofit().getToken(username, password)
    }

}

