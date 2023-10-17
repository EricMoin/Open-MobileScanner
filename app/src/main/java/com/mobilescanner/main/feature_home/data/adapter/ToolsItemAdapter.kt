package com.mobilescanner.main.feature_home.data.adapter

import android.graphics.BitmapFactory
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.item.ToolsItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_home.remote.model.ImageApi
import com.mobilescanner.main.feature_home.remote.model.OcrApi
import com.mobilescanner.main.main.data.utils.Constants

class ToolsItemAdapter(val context:FragmentActivity) :RecyclerView.Adapter<ToolsItemAdapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val itemIcon = view.findViewById<ImageView>(R.id.itemIcon)
        val itemDescription = view.findViewById<TextView>(R.id.itemDescription)
    }
    var onToolsClickLister: OnItemClickListener<ToolsItem>?= null
    private val list = listOf(
        ToolsItem( ImageApi(Constants.IMAGE_DEFINITION_ENHANCE), BitmapFactory.decodeResource(context.resources,R.drawable.document),"文档增强" ),
        ToolsItem( OcrApi(Constants.GENERAL_BASIC) ,BitmapFactory.decodeResource(context.resources,R.drawable.ocr),"文字识别" )
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tools_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.setOnClickListener{ onToolsClickLister?.onItemClicked(item) }
        Glide.with(context).load(item.icon).into(holder.itemIcon)
        holder.itemDescription.text = SpannableStringBuilder.valueOf(item.description)
    }
}