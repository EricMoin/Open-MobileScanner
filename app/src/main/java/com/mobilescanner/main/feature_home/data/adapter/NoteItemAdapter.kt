package com.mobilescanner.main.feature_home.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener

class NoteItemAdapter(private val list:MutableList<NoteItem>):RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.title)
        val body = view.findViewById<TextView>(R.id.body)
    }
    var onItemClickListener:OnItemClickListener<NoteItem> ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.body.text = item.body
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(list[position])
        }
    }
}