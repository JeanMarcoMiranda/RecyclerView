package com.example.prueba

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.Adapters.PhotoRecyclerAdapter
import com.example.prueba.ModelView.PhotosViewModel
import com.example.prueba.models.Photos
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_photo_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var photoAdapter : PhotoRecyclerAdapter

    private lateinit var model : PhotosViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loading = findViewById<ProgressBar>(R.id.loading)

        model = ViewModelProviders.of(this)[PhotosViewModel::class.java]

        loading.visibility = View.VISIBLE
        initRecyclerView()
        setRecyclerViewItemTouchListener()

        CoroutineScope(IO).launch{
            añadirData()
        }


        model.getInput().observe(  this@MainActivity , Observer<List<Photos>> {
            lista -> photoAdapter.setListData(lista)
        })

    }

    //Añadir a main
    private suspend fun setListToMainThread(respuesta : Response<List<Photos>>){
        withContext(Main){
            añadirLista(respuesta)
            loading.visibility = View.GONE
        }
    }

    private fun añadirLista(respuesta : Response<List<Photos>>){
        model.setInput(respuesta.body()!!)
        photoAdapter.setListData(respuesta.body()!!)

    }

    //Model View
    suspend private fun añadirData(){
        val response = DataSource().getPhotos()


        if( response.isSuccessful ){
            setListToMainThread(response)
        }
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            photoAdapter = PhotoRecyclerAdapter(this@MainActivity)
            adapter = photoAdapter

        }
    }

    private fun setRecyclerViewItemTouchListener() {

        //1
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                val builder = AlertDialog.Builder(this@MainActivity)

                val idPhoto = viewHolder.itemView.txtId.text.toString()

                // Set the alert dialog title
                builder.setTitle("Eliminar Registro")

                // Display a message on alert dialog
                builder.setMessage("Estas seguro de que deseas eliminar este registro")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){dialog, which ->

                    CoroutineScope(IO).launch {
                        val response = DataSource().deletePhoto(idPhoto.toInt())
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext,"Registro eliminado correctamente.",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext,"Error al eliminar registro.",Toast.LENGTH_SHORT).show()
                        }
                    }

                }


                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){dialog,which ->
                    dialog.dismiss()
                }


                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            }

        }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }
}
