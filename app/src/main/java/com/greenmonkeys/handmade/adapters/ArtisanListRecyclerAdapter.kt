package com.greenmonkeys.handmade.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greenmonkeys.handmade.ArtisanInformationActivity
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.R
import com.greenmonkeys.handmade.persistence.ImageStorage

class ArtisanListRecyclerAdapter(private val context: Context): RecyclerView.Adapter<ArtisanListRecyclerAdapter.ViewHolder>() {
    interface RecyclerViewClickListener {
        fun onClick(artisan: Artisan, context: Context)
    }

    private val values = ArrayList<Artisan>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artisan_recycler_item_view, parent, false)
        return ViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: ArtisanListRecyclerAdapter.ViewHolder, position: Int) {
        val artisan = values[position]
        holder.nameField?.text = artisan.getFullName()
        val imageName = "${artisan.email}.png"
        val imageBitmap = ImageStorage.retrieveImageFromInternalStorage(imageName, context)
        holder.imageField?.setImageBitmap(imageBitmap)
        holder.bind(values[position], object: RecyclerViewClickListener {
            override fun onClick(artisan: Artisan, context: Context) {
                val intent = Intent(context, ArtisanInformationActivity::class.java)
                intent.putExtra("EMAIL", artisan.email)
                intent.putExtra("CGA_ID", artisan.cgaId)
                context.startActivity(intent)
            }
        })
    }

    override fun getItemCount() = values.size

    fun updateValues(newValues: List<Artisan>) {
        values.clear()
        values.addAll(newValues)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView) {
        var nameField: TextView? = null
        var imageField: ImageView? = null

        init {
            nameField = itemView.findViewById(R.id.artisan_recycler_item_name)
            imageField = itemView.findViewById(R.id.artisan_recycler_profile_image)
        }

        fun bind(artisan: Artisan, listener: RecyclerViewClickListener) {
            itemView.setOnClickListener {
                listener.onClick(artisan, context)
            }
        }
    }
}