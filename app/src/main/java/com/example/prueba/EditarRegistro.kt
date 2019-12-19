package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prueba.models.Photos
import kotlinx.android.synthetic.main.activity_editar_registro.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditarRegistro : AppCompatActivity() {


    lateinit var response : Response<Photos>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_registro)

        val extras : Bundle? = this.intent.extras
        val id : Int? = extras?.getInt("id")

        val modo : String? = extras?.getString("Modo")

        CoroutineScope(IO).launch {
            response = DataSource().getPhoto(id!!)

            if(response.isSuccessful){
                val result = response.body()

                withContext(Main){
                    txtId.setText(result?.id.toString())
                    txtAlbumId.setText(result?.albumId.toString())
                    txtTitle.setText(result?.title.toString())

                    txtId.setEnabled(false)
                    txtAlbumId.setEnabled(false)
                    if(modo.equals("Ver")){
                        txtInfo.setText(modo)
                        txtTitle.setEnabled(false)
                    }else{
                        txtInfo.setText(modo)
                    }

                }
            }
        }

        btnVolver.setOnClickListener{
            finish()
        }

        btnEditar.setOnClickListener{

        }

    }
}
