package com.mobilescanner.main.main.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MobileScannerApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}