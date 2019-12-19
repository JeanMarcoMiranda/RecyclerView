package com.example.prueba.Adapters

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.prueba.EditarRegistro
import com.example.prueba.MainActivity
import com.example.prueba.R
import com.example.prueba.models.Photos
import kotlinx.android.synthetic.main.layout_photo_list_item.view.*
import kotlinx.coroutines.withContext

class PhotoRecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items : List<Photos> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from( parent.context).inflate(R.layout.layout_photo_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PhotoViewHolder ->{
                holder.bind(items.get(position))
                holder.itemView.setOnClickListener{
                    val intent = Intent(context, EditarRegistro::class.java)
                    val id = holder.photoId.text.toString().toInt()
                    System.out.println("Adapter ID :"+ id)
                    intent.putExtra("Modo", "Ver")
                    intent.putExtra("id", id)
                    context.startActivity(intent)
                }

                holder.photoButtonEditar.setOnClickListener{
                    val intent = Intent(context, EditarRegistro::class.java)
                    val id = holder.photoId.text.toString().toInt()
                    System.out.println("Adapter ID :"+ id)
                    intent.putExtra("Modo", "Ver")
                    intent.putExtra("id", id)
                    context.startActivity(intent)
                }


            }
        }
    }

    override fun getItemCount(): Int {
         return items.size
    }


    fun submitList(photoList : List<Photos>){
        items = photoList
    }

    fun setListData(photoList: List<Photos>) {
        items = photoList
        notifyDataSetChanged()
    }

    class PhotoViewHolder constructor(
        itemView : View
    ): RecyclerView.ViewHolder(itemView){

        //ID from our layout
        val photoAlbumId = itemView.txtAlbumId
        val photoId = itemView.txtId
        val photoTitle = itemView.txtTitle
        val photoImage = itemView.imgPhoto
        val photoButtonEditar = itemView.btnEditar


        fun bind(photo: Photos){

            photoAlbumId.setText(photo.albumId.toString())
            photoId.setText(photo.id.toString())
            photoTitle.setText(photo.title)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .load(photo.thumbnailUrl)
                .into(photoImage)

        }

    }

    interface onItemListener{
        fun onItemClick(position: Int)
    }


}