package com.mobilescanner.main.feature_home.ui.scan

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_project.data.repository.ImageRepository
import com.mobilescanner.main.feature_project.data.entity.ImageItem

class ScanViewModel: ViewModel() {
    var imageItem: ImageItem?= null
    var originBitmap: Bitmap?= null
    var operateBitmap: Bitmap?= null
    fun insertHistory(item: ImageItem) = ImageRepository.insertHistory(item)
    private val _imageLiveData = MutableLiveData< Pair<String,String> >()
    val imageLiveData get() = _imageLiveData.switchMap{ pair ->
        ImageRepository.enhanceImage(pair.first,pair.second)
    }
    fun enhanceImage(api:String,base64:String){
        _imageLiveData.value = Pair(api,base64)
    }
    private val _ocrLiveData = MutableLiveData< Pair<String,String> >()
    val ocrLiveData get() = _ocrLiveData.switchMap{ pair ->
        ImageRepository.scanOcr(pair.first,pair.second)
    }
    fun scanOcr(api:String,base64:String){
        _ocrLiveData.value = Pair(api,base64)
    }
    private val _saveLiveData = MutableLiveData<Unit>()
    val saveLiveData get() = _saveLiveData
    fun saveImage(){
        _saveLiveData.value = _saveLiveData.value
    }
}
/*
    当参数复杂度增加
    考虑使用Argument类统一管理api参数
 */