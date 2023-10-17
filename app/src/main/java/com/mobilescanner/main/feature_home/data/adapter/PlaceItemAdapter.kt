package com.mobilescanner.main.feature_home.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_home.remote.model.Place

class PlaceItemAdapter(private val list:MutableList<Place>) : RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val place = view.findViewById<TextView>(R.id.place)
        val address = view.findViewById<TextView>(R.id.address)
    }
    var onItemClickLister: OnItemClickListener<Place>?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.place.text = item.name
        holder.address.text = item.address
        holder.itemView.setOnClickListener { onItemClickLister?.onItemClicked(item) }
    }
}