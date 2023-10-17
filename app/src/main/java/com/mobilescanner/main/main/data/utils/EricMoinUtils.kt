package com.mobilescanner.main.main.data.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mobilescanner.main.main.data.service.ServiceCreator

object EricMoinUtils {
    fun <T> Result<T>.onSuccess( block:( dat:T? ) -> Unit ):Result<T>{
        if ( this.isSuccess ) {
            val data = this.getOrNull()
            block(data)
        }
        return this
    }
    fun <T> Result<T>.onFailed( block:() -> Unit ):Result<T>{
        if ( this.isFailure ) block()
        return this
    }
    fun <T> LiveData<T>.solve( lifecycleOwner:LifecycleOwner, solver: ( args:T ) -> Unit ) {
        this.observe( lifecycleOwner, Observer { solver(it) } )
    }
    fun <T> Fragment.start( context:FragmentActivity,cls:Class<T> ) = Intent(context,cls).apply { context.startActivity(this) }
    fun <T> Fragment.start( cls:Class<T> ) = Intent(context,cls).let { this@start.startActivity(it) }
    fun Fragment.toast( msg:String,duration: Int = Toast.LENGTH_SHORT ) = msg.toast(this.requireActivity(),duration)
    fun Activity.toast( msg:String,duration: Int = Toast.LENGTH_SHORT ) = msg.toast(this,duration)

    fun <T> View.find( id:Int, block:(view:View) -> Unit ){

    }
    fun String.toast(context:FragmentActivity,duration:Int = Toast.LENGTH_SHORT ){ Toast.makeText(context,this,duration).show()  }
    fun String.toast(context:Activity,duration:Int = Toast.LENGTH_SHORT ){ Toast.makeText(context,this,duration).show()  }
}