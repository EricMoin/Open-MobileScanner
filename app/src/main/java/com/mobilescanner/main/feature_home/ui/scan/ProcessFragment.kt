package com.mobilescanner.main.feature_home.ui.scan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.feature_project.data.entity.toFile
import com.mobilescanner.main.feature_project.ui.ProjectViewModel
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.toFile

class ProcessFragment : Fragment() {

    companion object {
        fun newInstance() = ProcessFragment()
    }
    private lateinit var mainView:View
    private lateinit var scanViewModel:ScanViewModel
    private lateinit var projectViewModel: ProjectViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_process, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        scanViewModel = ViewModelProvider(requireActivity())[ScanViewModel::class.java]
        projectViewModel = ViewModelProvider(requireActivity())[ProjectViewModel::class.java]
        initTitle()
        initImage()
        initPanel()
    }

    private fun initTitle() {
        val title = mainView.findViewById<TextView>(R.id.title)
        title.text = scanViewModel.imageItem?.title
    }

    private fun initImage() {
        val image = mainView.findViewById<ImageView>(R.id.image)
        scanViewModel.operateBitmap?.let { bitmap ->
            Glide.with(this).load(bitmap).into(image)
        }
    }

    private fun initPanel() {
        val share = mainView.findViewById<ImageView>(R.id.share)
        share.setOnClickListener {
            FileUtils.shareFile(scanViewModel.imageItem!!.toFile(),requireActivity())
        }
        val camera = mainView.findViewById<ImageView>(R.id.camera)
        camera.setOnClickListener {
            findNavController().popBackStack(R.id.captureFragment, inclusive = false )
        }
        val newProject = mainView.findViewById<ImageView>(R.id.newProject)
        newProject.setOnClickListener {
            projectViewModel.project =  ProjectItem(
                title = scanViewModel.imageItem!!.title,
                body = "",
                imageList = mutableListOf(scanViewModel.imageItem!!.filePath)
            )
            "project is ${projectViewModel.project}".logD("ProjectEdit")
            findNavController().popBackStack(R.id.mainFragment, inclusive = false )
            findNavController().navigate(R.id.action_mainFragment_to_projectEditFragment)
        }
    }
}