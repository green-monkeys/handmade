package com.greenmonkeys.handmade.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.R

class ArtisanListRecyclerAdapter(private val values: List<Artisan>): RecyclerView.Adapter<ArtisanListRecyclerAdapter.ViewHolder>() {
    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artisan_recycler_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtisanListRecyclerAdapter.ViewHolder, position: Int) {
        holder.textView?.text = values[position].getFullName()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        init {
            textView = itemView.findViewById(R.id.artisan_recycler_item_name)
        }
    }
}