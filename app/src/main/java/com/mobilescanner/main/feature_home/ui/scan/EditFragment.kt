package com.mobilescanner.main.feature_home.ui.scan

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.adapter.ToolsItemAdapter
import com.mobilescanner.main.feature_home.data.item.ToolsItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_home.remote.model.ImageApi
import com.mobilescanner.main.feature_home.remote.model.ImageResponse
import com.mobilescanner.main.feature_home.remote.model.OcrApi
import com.mobilescanner.main.feature_home.remote.model.OcrGeneralBasicResponse
import com.mobilescanner.main.feature_home.ui.dialog.OcrDialog
import com.mobilescanner.main.feature_home.ui.dialog.ProcessDialog
import com.mobilescanner.main.main.data.utils.BitmapUtils
import com.mobilescanner.main.main.data.utils.BitmapUtils.base64ToBitmap
import com.mobilescanner.main.main.data.utils.BitmapUtils.contrast
import com.mobilescanner.main.main.data.utils.BitmapUtils.rotatedBitmap
import com.mobilescanner.main.main.data.utils.BitmapUtils.toBase64
import com.mobilescanner.main.main.data.utils.Constants
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onSuccess
import com.mobilescanner.main.main.data.utils.EricMoinUtils.solve
import com.mobilescanner.main.main.data.utils.EricMoinUtils.toast
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class EditFragment : Fragment() {

    companion object {
        fun newInstance() = EditFragment()
    }
    private lateinit var viewModel:ScanViewModel
    private lateinit var mainView:View
    private lateinit var ocrDialog:OcrDialog
    private lateinit var processDialog:ProcessDialog

    private val onToolsClickListener =  object : OnItemClickListener<ToolsItem>{
        override fun onItemClicked(item: ToolsItem) {
            processDialog?.show(childFragmentManager,"")
            lifecycleScope.launch {
                when(item.api){
                    is ImageApi -> {
                        val base64 = viewModel.operateBitmap!!.toBase64()
                        viewModel.enhanceImage(item.api.label,base64)
                    }
                    is OcrApi -> {
                        val base64 = viewModel.operateBitmap!!.toBase64()
                        viewModel.scanOcr(item.api.label,base64)
                    }
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[ScanViewModel::class.java]
        initImage()
        initRecycler()
        initPanel()
        observeLiveData()
    }

    private fun observeLiveData() {
        val image = mainView.findViewById<ImageView>(R.id.image)
        image.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    Glide.with(this@EditFragment).load(viewModel.originBitmap).into(image)
                }
                MotionEvent.ACTION_UP -> {
                    Glide.with(this@EditFragment).load(viewModel.operateBitmap).into(image)
                }
            }
            true
        }
        ocrDialog = OcrDialog(requireActivity())
        processDialog = ProcessDialog()
        viewModel.imageLiveData.observe(
            viewLifecycleOwner, Observer { result ->
                if ( result.isFailure ){
                    Toast.makeText(requireActivity(),"网络错误",Toast.LENGTH_SHORT).show()
                }else{
                    val item = result.getOrNull() as ImageResponse
                    lifecycleScope.launch {
                        viewModel.operateBitmap = withContext(Dispatchers.Default){
                            item.image.base64ToBitmap().contrast(50.toDouble())
                        }
                        refreshImage()
                        toast("按住图片以查看原图")
                    }
                }
                processDialog.dismiss()
            }
        )
        viewModel.ocrLiveData.solve(viewLifecycleOwner){ result ->
            result.onSuccess { data ->
                val response = data as OcrGeneralBasicResponse
                processDialog.dismiss()
                ocrDialog.show()
                ocrDialog.setText(response.wordsResult)
            }.onFailure {
                Toast.makeText(requireActivity(),"网络错误",Toast.LENGTH_SHORT).show()
            }
            processDialog.dismiss()
        }
    }

    private fun initPanel() {
        val save = mainView.findViewById<ImageView>(R.id.save)
        save.setOnClickListener {
            viewModel.imageItem?.let { item ->
                viewModel.insertHistory(item)
                saveImage()
            }
        }
        val turnLeft = mainView.findViewById<ImageView>(R.id.turnLeft)
        turnLeft.setOnClickListener {
            viewModel.operateBitmap = BitmapUtils.rotateBitmap(viewModel.operateBitmap!!,270)
            refreshImage()
        }
        val turnRight = mainView.findViewById<ImageView>(R.id.turnRight)
        turnRight.setOnClickListener {
            viewModel.operateBitmap = BitmapUtils.rotateBitmap(viewModel.operateBitmap!!,90)
            refreshImage()
        }
    }
    fun refreshImage(){
        val image = mainView.findViewById<ImageView>(R.id.image)
        viewModel.operateBitmap?.let { bitmap ->
            Glide.with(this@EditFragment).load(bitmap).into(image)
        }
    }
    private fun saveImage() {
        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                FileOutputStream(viewModel.imageItem?.filePath).use{ fos ->
                    viewModel.operateBitmap?.compress(Bitmap.CompressFormat.JPEG,100,fos)
                    fos.flush()
                }
            }
            viewModel.saveImage()
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_captureFragment_to_processFragment)
        }
    }

    private fun initImage() {
        val imageName = mainView.findViewById<EditText>(R.id.imageName)
        viewModel.imageItem?.let { item ->
            imageName.text = Editable.Factory.getInstance().newEditable(item.title)
            viewModel.originBitmap = item.filePath.rotatedBitmap()
            viewModel.operateBitmap =viewModel.originBitmap
            refreshImage()
        }
    }

    private fun initRecycler() {
        val operateRecycler = mainView.findViewById<RecyclerView>(R.id.operateRecycler)
        operateRecycler.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = ToolsItemAdapter(requireActivity())
        adapter.onToolsClickLister = onToolsClickListener
        operateRecycler.adapter =adapter
    }
}