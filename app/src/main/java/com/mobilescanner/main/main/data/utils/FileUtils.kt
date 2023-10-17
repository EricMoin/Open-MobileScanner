package com.mobilescanner.main.main.data.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilescanner.main.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

object FileUtils {
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm"
    fun getOutputDirectory(activity: Activity):File{
        val cacheDir = activity.externalCacheDir?.let{ it ->
            File(it,activity.resources.getString( R.string.app_name) ).apply { mkdirs() }
        }
        return if( cacheDir != null && cacheDir.exists() ) cacheDir else activity.filesDir
    }
    fun getCurrentTime(): String = SimpleDateFormat( FILENAME_FORMAT, Locale.CHINA ).format( System.currentTimeMillis() )
    fun getOutputFile(activity: Activity,fileName:String) = File(
        getOutputDirectory(activity),
        fileName
    )
    fun convertFile2Base64(path: String): String {
        var result = ""
        FileInputStream(path).use { fis ->
            val data = ByteArray(fis.available())
            result = Base64.encodeToString(data, Base64.DEFAULT)
        }
        return result
    }
    fun convertBase64ToFile(base64Str:String,outputPath:String):File {
        val data = Base64.decode(base64Str, Base64.DEFAULT);
        FileOutputStream(outputPath).use { fos ->
            fos.write(data)
        }
        return File(outputPath)
    }
    fun shareFile(file:File,context: Context){
        val mediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val uri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER_AUTHORITY,file)
        val intent = Intent().apply {
            type = mediaType
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM,uri)
        }
        context.startActivity(Intent.createChooser( intent, context.getString(R.string.share_hint)) )
    }
    fun String.toFile() = File(this)
    fun getFilePathFromUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val resolver = context.contentResolver
        var input: FileInputStream? = null
        var output: FileOutputStream? = null
        try {
            val pfd = resolver.openFileDescriptor(uri, "r") ?: return null
            val fd = pfd.fileDescriptor
            input = FileInputStream(fd)
            val outputDir = context.cacheDir
            val outputFile = File.createTempFile("image", "tmp", outputDir)
            val tempFilename = outputFile.absolutePath
            output = FileOutputStream(tempFilename)
            var read: Int
            val bytes = ByteArray(4096)
            while (input.read(bytes).also { read = it } != -1) {
                output.write(bytes, 0, read)
            }
            return File(tempFilename).absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                input?.close()
                output?.close()
            } catch (t: Throwable) {
                // Do nothing
                t.printStackTrace()
            }
        }
        return null
    }
    fun forResult( context:FragmentActivity,block:( uri:Uri ) -> Unit  ) =
        context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if ( it.resultCode == Activity.RESULT_OK ){
                it?.data?.data?.let { uri -> block(uri) }
            }
        }
    fun forResult( context:Fragment,block:( uri:Uri ) -> Unit  ) =
        context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if ( it.resultCode == Activity.RESULT_OK ){
                it?.data?.data?.let { uri -> block(uri) }
            }
        }
    fun callAlbum( launcher:ActivityResultLauncher<Intent> ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        launcher.launch(intent)
    }
    fun String.toBitmap(opts: BitmapFactory.Options?=null) = BitmapFactory.decodeFile(this,opts)
    fun File.toBitmap(opts: BitmapFactory.Options?=null) = BitmapFactory.decodeFile(this.absolutePath,opts)
    fun String.logE(tag:String="Log") = Log.e(tag,this)
    fun String.logD(tag:String="Log") = Log.d(tag,this)
}