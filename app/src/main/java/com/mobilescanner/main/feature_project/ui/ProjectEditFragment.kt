package com.mobilescanner.main.feature_project.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.ui.scan.ScanViewModel
import com.mobilescanner.main.feature_project.data.adapter.PDFImageItemAdapter
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.feature_project.data.entity.toImageItem
import com.mobilescanner.main.feature_project.data.listener.OnImageReceiveCallback
import com.mobilescanner.main.feature_project.data.utils.PDFHelper
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import com.mobilescanner.main.main.data.utils.FileUtils.toFile
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.Collections

class ProjectEditFragment : Fragment() {

    companion object {
        fun newInstance() = ProjectEditFragment()
    }

    private lateinit var viewModel: ProjectViewModel
    private lateinit var mainView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[ProjectViewModel::class.java]
        initTitleBar()
        initRecycler()
    }

    private fun initTitleBar() {
        val pdf = mainView.findViewById<ImageView>(R.id.pdf)
        pdf.setOnClickListener {
            lifecycleScope.launch {
                PDFHelper.convertJPGtoPDF( requireActivity(), viewModel.project.imageList )
            }
            Toast.makeText(requireActivity(),"保存成功.",Toast.LENGTH_SHORT).show()
1        }
        val title = mainView.findViewById<TextView>(R.id.title)
        title.text = viewModel.project.title
        title.addTextChangedListener { content ->
            content?.let {
                viewModel.project.title = it.toString()
            }
        }

        val share = mainView.findViewById<ImageView>(R.id.share)
        share.setOnClickListener {
            FileUtils.shareFile(PDFHelper.outputFile,requireActivity())
        }
        val ensure = mainView.findViewById<ImageView>(R.id.ensure)
        ensure.setOnClickListener {
            viewModel.updateProject()
            findNavController().popBackStack()
        }
    }

    private fun initRecycler() {
        "project is ${viewModel.project}".logD("Project")
        viewModel.pdfImageList.clear()
        viewModel.pdfImageList.add(ImageItem("",""))
        viewModel.project.imageList.forEach { it ->
            viewModel.pdfImageList.add(0,it.toFile().toImageItem())
        }
        val pdfImageRecycler = mainView.findViewById<SwipeRecyclerView>(R.id.pdfImageRecycler)
        val adapter = PDFImageItemAdapter(this,viewModel.pdfImageList )
        val linearLayout = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        val gridLayout = GridLayoutManager( requireActivity() ,2)
        pdfImageRecycler.layoutManager = linearLayout
        pdfImageRecycler.adapter = adapter
        pdfImageRecycler.isLongPressDragEnabled = true
        pdfImageRecycler.isItemViewSwipeEnabled = true
        pdfImageRecycler.setOnItemMoveListener(
            object :OnItemMoveListener{
                override fun onItemMove(
                    srcHolder: RecyclerView.ViewHolder,
                    targetHolder: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPosition = srcHolder.adapterPosition
                    val toPosition = targetHolder.adapterPosition
                    if ( toPosition == viewModel.pdfImageList.lastIndex ||
                        fromPosition == viewModel.pdfImageList.lastIndex  ) return true
                    Collections.swap(viewModel.project.imageList,fromPosition,toPosition)
                    Collections.swap(viewModel.pdfImageList,fromPosition,toPosition)
                    adapter.notifyItemMoved(fromPosition, toPosition)
                    return true
                }
                override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {
                    val index = srcHolder.adapterPosition
                    if ( index == 0 ) return
                    "${viewModel.project.imageList.size}".logD("ProjectEdit")
                    "${viewModel.pdfImageList.size}".logD("ProjectEdit")
                    viewModel.project.imageList.removeAt(index)
                    viewModel.pdfImageList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }

            }
        )
        adapter.setCallback(
            object :OnImageReceiveCallback{
                override fun onImageReceive(filePath: String) {
                    viewModel.project.imageList.add(filePath)
                    viewModel.pdfImageList.add(viewModel.pdfImageList.lastIndex,ImageItem(filePath,""))
                    adapter.notifyItemInserted(viewModel.pdfImageList.lastIndex-1)
                }
            }
        )
        val transform = mainView.findViewById<ImageView>(R.id.transform)
        transform.setOnClickListener {
            pdfImageRecycler.layoutManager = if ( pdfImageRecycler.layoutManager == linearLayout ) gridLayout else linearLayout
            adapter.notifyItemRangeChanged(0,viewModel.pdfImageList.size)
        }
    }
}