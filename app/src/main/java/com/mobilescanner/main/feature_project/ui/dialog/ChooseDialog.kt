package com.mobilescanner.main.feature_project.ui.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.ui.scan.ScanViewModel
import com.mobilescanner.main.feature_project.data.listener.OnImageReceiveCallback
import com.mobilescanner.main.feature_project.ui.ProjectViewModel
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD

class ChooseDialog():DialogFragment(){
    private lateinit var mainView:View
    private lateinit var viewModel:ProjectViewModel
    private lateinit var scanViewModel: ScanViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var callback:OnImageReceiveCallback
    fun setCallback(callback:OnImageReceiveCallback){
        this.callback = callback
    }
    constructor(callback: OnImageReceiveCallback) : this() {
        this.callback = callback
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                it.data?.data?.let { uri ->
                    val filePath = FileUtils.getFilePathFromUri( requireActivity() ,uri )
                    "Selected $filePath".logD("Choose")
                    filePath?.let { callback.onImageReceive(it) }
                    dismiss()
                }
            }
        }
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_dialog,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = requireDialog().window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[ProjectViewModel::class.java]
        scanViewModel = ViewModelProvider(this)[ScanViewModel::class.java]
        initDialog()
    }
    private fun initDialog() {
        val camera = mainView.findViewById<MaterialCardView>(R.id.camera)
        val album = mainView.findViewById<MaterialCardView>(R.id.album)
        camera?.setOnClickListener {
            findNavController().navigate(R.id.action_projectEditFragment_to_captureFragment)
        }
        album?.setOnClickListener{
            FileUtils.callAlbum(launcher)
        }
        scanViewModel.saveLiveData.observe(
            viewLifecycleOwner, Observer {
                callback.onImageReceive(scanViewModel.imageItem!!.filePath)
            }
        )
    }
}