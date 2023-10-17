package com.mobilescanner.main.feature_project.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.feature_project.data.listener.OnImageReceiveCallback
import com.mobilescanner.main.feature_project.ui.dialog.ChooseDialog
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.toFile

class PDFImageItemAdapter(val fragment: Fragment, private val list:MutableList<ImageItem>): RecyclerView.Adapter<PDFImageItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val pdfImage = view.findViewById<ImageView>(R.id.pdfImage)
    }
    private lateinit var callback: OnImageReceiveCallback
    fun setCallback(callback: OnImageReceiveCallback){
        this.callback = callback
    }
    private val dialog = ChooseDialog(
        object:OnImageReceiveCallback {
            override fun onImageReceive(filePath: String) {
                callback?.onImageReceive(filePath)
            }
        }
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pdf_image_item,parent,false))
        val hintHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pdf_image_insert_hint,parent,false))
        hintHolder.itemView.setOnClickListener {
            dialog.showNow(fragment.childFragmentManager,"")
        }
        return if ( viewType ==  0 ) hintHolder else viewHolder
    }

    override fun getItemCount() = list.size
    override fun getItemViewType(position: Int): Int {
        return if ( list[position].filePath.isEmpty() ) 0 else 1
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        if ( holder.itemViewType == 0 ) return
        Glide.with(fragment).load(item.filePath.toFile()).into(holder.pdfImage)
    }
}