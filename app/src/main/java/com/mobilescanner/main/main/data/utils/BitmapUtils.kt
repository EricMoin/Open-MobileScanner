package com.mobilescanner.main.main.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.exifinterface.media.ExifInterface
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow


object BitmapUtils {
    fun compressImageQuality(filePath:String,reqWidth:Int, reqHeight:Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(filePath,options)
        options.inSampleSize = computeSampleSize(options,reqWidth,reqHeight)
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = false;
        bitmap.recycle()
        try {
            val newBitmap = BitmapFactory.decodeFile(filePath,options)
            bitmap = newBitmap
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("ImageItem","Out of Memory!!!")
        }
        return bitmap
    }
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth:Int, reqHeight:Int):Int{
        val width = options.outWidth;
        val height = options.outHeight;
        var inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            val widthRadio = Math.round(width * 1.0f / reqWidth);
            val heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    /**
     * 按图片尺寸压缩 参数是bitmap
     * @param bitmap
     * @param pixelW
     * @param pixelH
     * @return
     */
    fun compressImageSize(
        filePath:String,
        pixelW: Int,
        pixelH: Int
    ): Bitmap {
        val bitmap = BitmapFactory.decodeFile(filePath)
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        if (os.toByteArray().size / 1024 > 512) { //判断如果图片大于0.5M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                50,
                os
            ) //这里压缩50%，把压缩后的数据存放到baos中
        }
        var bis = ByteArrayInputStream(os.toByteArray())
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inPreferredConfig = Bitmap.Config.RGB_565
        BitmapFactory.decodeStream(bis, null, options)
        options.inJustDecodeBounds = false
        options.inSampleSize =
            computeSampleSize(options, if (pixelH > pixelW) pixelW else pixelH, pixelW * pixelH)
        bis = ByteArrayInputStream(os.toByteArray())
        return BitmapFactory.decodeStream(bis, null, options)!!
    }

    fun computeSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int,
        maxNumOfPixels: Int
    ): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl  1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    private fun computeInitialSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int,
        maxNumOfPixels: Int
    ): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound =
            if (maxNumOfPixels == -1) 1 else Math.ceil(Math.sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1) 128 else Math.min(
            Math.floor(w / minSideLength),
            Math.floor(h / minSideLength)
        ).toInt()
        if (upperBound < lowerBound) {
            return lowerBound
        }
        return when{
            maxNumOfPixels == -1 && minSideLength == -1 -> 1
            minSideLength == -1 -> lowerBound
            else -> upperBound
        }
    }
    fun getFileContentAsBase64(path: String, urlEncode: Boolean = true): String {
        val b = Files.readAllBytes(Paths.get(path))
        var base64 = java.util.Base64.getEncoder().encodeToString(b)
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8")
        }
        return base64
    }
    suspend fun Bitmap.toBase64(
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 100
    ): String {
        val bitmap = this
        val result = CoroutineScope(Dispatchers.IO).async {
            ByteArrayOutputStream().use { outputStream ->
                bitmap.compress(format, quality, outputStream)
                val buffer = outputStream.toByteArray()
                "buffer 大小 ${buffer.size}".logE()
                Base64.encodeToString(buffer, Base64.DEFAULT)
            }
        }
        return result.await()
    }

    suspend fun String.base64ToBitmap(): Bitmap {
        val str = this
        val result = CoroutineScope(Dispatchers.IO).async {
            val byteArray = Base64.decode(str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        return result.await()
    }
    fun Bitmap.contrast(value:Double):Bitmap{
        val bmOut = Bitmap.createBitmap(width,height,config)
        val contrast = ((100 + value) / 100).pow(2.0)
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                val pixel = getPixel(x, y)
                val A = Color.alpha(pixel)
                // apply filter contrast for every channel R, G, B
                var R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, R, R))
            }
        }
        recycle()
        return bmOut
    }
    fun File.rotatedBitmap() = rotateIfRequired(this.absolutePath,BitmapFactory.decodeFile(this.absolutePath))
    fun String.rotatedBitmap() = rotateIfRequired(this,BitmapFactory.decodeFile(this))
    fun rotateIfRequired(filePath: String,bitmap:Bitmap):Bitmap{
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap,270)
            else -> bitmap
        }
    }
    fun rotateBitmap(bitmap:Bitmap,degree:Int):Bitmap{
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val result = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return result
    }
    fun getBitmapFromUri(context: Context, uri: Uri) = context.contentResolver.openFileDescriptor(uri,"r")?.use{
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
    fun Bitmap.scaleByWidth(targetView: View):Bitmap{
        "[image] height = $height width = $width".logD("Utils")
        "[view] height = ${targetView.measuredHeight}".logD("Utils")
        val measureWidth = targetView.measuredWidth
        val scale = measureWidth.toDouble() / this.width.toDouble()
        val measureHeight =  (this.height * scale).toInt()
        return Bitmap.createScaledBitmap(this,measureWidth,measureHeight,false)
    }
}