package com.mobilescanner.main.feature_project.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_project.data.entity.ProjectItem

class ProjectItemAdapter(private val list:MutableList<ProjectItem>):RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val projectName = view.findViewById<TextView>(R.id.projectName)
        val projectBody = view.findViewById<TextView>(R.id.projectBody)
        val itemCheckBox = view.findViewById<MaterialCheckBox>(R.id.itemCheckBox)
    }
    lateinit var onItemClickListener: OnItemClickListener<ProjectItem>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.projectName.text = item.title
        holder.projectBody.text = item.body
        holder.itemView.setOnClickListener { onItemClickListener.onItemClicked(item) }
    }
}