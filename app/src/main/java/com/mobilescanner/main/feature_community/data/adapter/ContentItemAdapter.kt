package com.mobilescanner.main.feature_community.data.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_community.data.item.ContentItem
import com.mobilescanner.main.feature_home.data.adapter.NoteItemAdapter
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.main.data.utils.BitmapUtils.scaleByWidth
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.toBitmap

class ContentItemAdapter(val context: Fragment, private val list:MutableList<ContentItem>): RecyclerView.Adapter<ContentItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.image)
        val text = view.findViewById<TextView>(R.id.text)
    }
    var onItemClickListener: OnItemClickListener<ContentItem>?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Glide.with(context).asBitmap().load(item.imagePath).into(
            object :ImageViewTarget<Bitmap>(holder.image){
                override fun setResource(resource: Bitmap?) {
                    resource?.scaleByWidth(holder.itemView)?.apply {
                        holder.image.setImageBitmap(this)
                    }
                }
            }
        )
        holder.text.text = item.description
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClicked(list[position]) }
    }
}