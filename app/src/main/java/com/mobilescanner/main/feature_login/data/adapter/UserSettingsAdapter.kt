package com.mobilescanner.main.feature_login.data.adapter
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener

class UserSettingsAdapter(private val list:MutableList<Pair<String,String>>):RecyclerView.Adapter<UserSettingsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.title)
        val content = view.findViewById<TextView>(R.id.content)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_setting_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.first
        holder.content.text = Editable
            .Factory
            .getInstance()
            .newEditable(item.second)
    }
}