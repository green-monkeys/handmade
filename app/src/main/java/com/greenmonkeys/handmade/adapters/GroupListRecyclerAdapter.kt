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
import com.greenmonkeys.handmade.GroupInformationActivity
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.R
import com.greenmonkeys.handmade.persistence.Group
import com.greenmonkeys.handmade.persistence.ImageStorage

class GroupListRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<GroupListRecyclerAdapter.ViewHolder>() {
    interface RecyclerViewClickListener {
        fun onClick(group: Group, context: Context)
    }

    private val values = ArrayList<Group>()

    private val checkedGroups = ArrayList<Group>()

    private var selectionButtonVisibility = View.GONE
    private var resetCheckboxes = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.group_recycler_item_view, parent, false)
        return ViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: GroupListRecyclerAdapter.ViewHolder, position: Int) {
        val group = values[position]
        holder.position = position
        holder.nameField.text = group.groupName

        holder.isSelectable.visibility = selectionButtonVisibility
        if (resetCheckboxes) {
            resetCheckboxes = false
            holder.isSelectable.isChecked = false
        }

        holder.bind(values[position], object : RecyclerViewClickListener {
            override fun onClick(group: Group, context: Context) {
                val intent = Intent(context, GroupInformationActivity::class.java)
                intent.putExtra("GROUP_NAME", group.groupName)
                intent.putExtra("CGA_EMAIL", group.cgaId)
                context.startActivity(intent)
            }
        })
    }

    override fun getItemCount() = values.size

    fun updateValues(newValues: List<Group>) {
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

    fun getSelected(): List<Group> {
        return checkedGroups
    }

    class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        var position: Int? = null
        var nameField: TextView = itemView.findViewById(R.id.group_recycler_group_name)
        var isSelectable: CheckBox = itemView.findViewById(R.id.group_recycler_selection)

        fun bind(group: Group, listener: RecyclerViewClickListener) {
            itemView.setOnClickListener {
                isSelectable.isChecked = !isSelectable.isChecked
                listener.onClick(group, context)
            }
            isSelectable.setOnClickListener {
                listener.onClick(group, context)
            }
        }
    }
}

