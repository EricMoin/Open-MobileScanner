package com.mobilescanner.main.feature_project.data.utils

import android.app.Activity
import android.widget.Toast
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.mobilescanner.main.main.data.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object PDFHelper {
    private const val defaultName = "移动扫描王"
    private var outputFileName = defaultName
    private const val PDF = ".pdf"
    lateinit var outputFile:File
    suspend fun convertJPGtoPDF(context: Activity,pathList:List<String>) =
        withContext(Dispatchers.IO){
            val root = FileUtils.getOutputDirectory(context).absolutePath+File.separator
            val filePath = StringBuilder()
                .append(root)
                .append(outputFileName)
                .append(PDF)
                .toString()
            outputFile = File(filePath)
            val writer = PdfWriter(outputFile)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)
            for( path in pathList ){
                val imageData = ImageDataFactory.create(path)
                val image = Image(imageData)
                document.add( image )
            }
            document.flush()
            document.close()
        }
    fun setOutputFileName(name:String){
        outputFileName = name
    }
}