package com.greenmonkeys.handmade.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greenmonkeys.handmade.ArtisanInformationActivity
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.R
import com.greenmonkeys.handmade.persistence.ImageStorage

class ArtisanListRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<ArtisanListRecyclerAdapter.ViewHolder>() {
    interface RecyclerViewClickListener {
        fun onClick(artisan: Artisan, context: Context)
    }

    private val values = ArrayList<Artisan>()

    private val checkedArtisans = ArrayList<Artisan>()

    private var selectionButtonVisibility = View.GONE
    private var resetCheckboxes = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artisan_recycler_item_view, parent, false)
        return ViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: ArtisanListRecyclerAdapter.ViewHolder, position: Int) {
        val artisan = values[position]
        holder.position = position
        holder.nameField.text = artisan.getFullName()
        val imageName = "${artisan.email}.png"
        val imageBitmap = ImageStorage.retrieveImageFromInternalStorage(imageName, context)
        holder.imageField.setImageBitmap(imageBitmap)

        holder.isSelectable.visibility = selectionButtonVisibility
        if (resetCheckboxes) {
            resetCheckboxes = false
            holder.isSelectable.isChecked = false
        }

        holder.bind(values[position], object : RecyclerViewClickListener {
            override fun onClick(artisan: Artisan, context: Context) {
                if (selectionButtonVisibility == View.GONE) {
                    val intent = Intent(context, ArtisanInformationActivity::class.java)
                    intent.putExtra("EMAIL", artisan.email)
                    intent.putExtra("CGA_ID", artisan.cgaId)
                    context.startActivity(intent)
                } else {
                    if (checkedArtisans.contains(artisan)) {
                        checkedArtisans.remove(artisan)
                    } else {
                        checkedArtisans.add(artisan)
                    }
                }
            }
        })
    }

    override fun getItemCount() = values.size

    fun updateValues(newValues: List<Artisan>) {
        values.clear()
        values.addAll(newValues)
        notifyDataSetChanged()
    }

    fun setSelectable(isSelectable: Boolean) {
        resetCheckboxes = true
        selectionButtonVisibility = if (isSelectable) {
            View.VISIBLE
        } else {
            View.GONE
        }
        notifyDataSetChanged()
    }

    fun getSelected(): List<Artisan> {
        return checkedArtisans
    }

    class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        var position: Int? = null
        var nameField: TextView = itemView.findViewById(R.id.artisan_recycler_item_name)
        var imageField: ImageView = itemView.findViewById(R.id.artisan_recycler_profile_image)
        var isSelectable: CheckBox = itemView.findViewById(R.id.artisan_recycler_selection)

        fun bind(artisan: Artisan, listener: RecyclerViewClickListener) {
            itemView.setOnClickListener {
                isSelectable.isChecked = !isSelectable.isChecked
                listener.onClick(artisan, context)
            }
            isSelectable.setOnClickListener {
                listener.onClick(artisan, context)
            }
        }
    }
}