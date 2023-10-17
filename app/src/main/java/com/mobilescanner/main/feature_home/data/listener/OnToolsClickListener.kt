package com.mobilescanner.main.feature_home.data.listener

import android.view.View
import com.mobilescanner.main.feature_home.data.item.ToolsItem

interface OnItemClickListener<T>{
    fun onItemClicked(item:T)
}