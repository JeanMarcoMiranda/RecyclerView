package com.example.prueba.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.LoginRepository
import com.example.prueba.data.Result

import com.example.prueba.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class LoginViewModel(private val loginRepository: LoginRepository,
                    private val application: Application) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default){
                loginRepository.login(username, password)
            }

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                    storeToken(result.data.token)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun storeToken(token : String){
        val secretKey = createKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptationIV = cipher.iv
        val tokenBytes = token.toByteArray()
        val encryptedTokenBytes = cipher.doFinal(tokenBytes)
        val encryptedToken = Base64.encodeToString(encryptedTokenBytes, Base64.DEFAULT)

        val utils = Utils(application)
        utils.saveStringInSp("token", encryptedToken)
        utils.saveStringInSp("encryptionIv", Base64.encodeToString(encryptationIV, Base64.DEFAULT))

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun createKey() : SecretKey{
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore")

        val keyGenParameterSpec = KeyGenParameterSpec.Builder("key",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

}
