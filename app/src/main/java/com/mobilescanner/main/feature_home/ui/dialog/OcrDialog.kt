package com.mobilescanner.main.feature_home.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.remote.model.OcrGeneralBasicResponse

class OcrDialog(val context: FragmentActivity): BottomSheetDialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ocr_dialog)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
    fun setText(textList:List<OcrGeneralBasicResponse.WordsResult>){
        val resultText = findViewById<EditText>(R.id.resultText)!!
        textList.forEach { item ->
            resultText.text.append(item.words)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isOutOfBounds(context, event)) {
            dismiss()
        }
        return super.onTouchEvent(event)
    }
    private fun isOutOfBounds(context: Context, event: MotionEvent):Boolean{
        val x = event.x
        val y = event.y
        val slop = ViewConfiguration.get(context).scaledWindowTouchSlop
        val decorView = window!!.decorView
        return (x<-slop)||(y<-slop)||(x>(decorView.width+slop+slop))||(y>(decorView.width+slop+slop))
    }
}