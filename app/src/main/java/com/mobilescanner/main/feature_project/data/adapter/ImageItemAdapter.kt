package com.mobilescanner.main.feature_project.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.toFile


class ImageItemAdapter(val context:FragmentActivity,private val list:MutableList<ImageItem>): RecyclerView.Adapter<ImageItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val historyImage = view.findViewById<ImageView>(R.id.historyImage)
    }
    lateinit var onItemClickListener:OnItemClickListener<ImageItem>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Glide.with(context).load(item.filePath.toFile()).into(holder.historyImage)
        holder.itemView.setOnClickListener { onItemClickListener.onItemClicked(item) }
    }
}