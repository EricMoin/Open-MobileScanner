package com.mobilescanner.main.feature_project.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_project.data.adapter.ImageItemAdapter
import com.mobilescanner.main.feature_project.data.adapter.ProjectItemAdapter
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.main.data.utils.EricMoinUtils.solve
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener
import java.util.Collections

class ProjectFragment : Fragment() {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    private lateinit var viewModel: ProjectViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.let {
            it.loadAllHistory()
            it.loadAllProject()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view

        viewModel = ViewModelProvider(requireActivity())[ProjectViewModel::class.java]
        initHistory()
        initProject()
    }

    private fun initProject() {
        val projectRecycler = mainView.findViewById<SwipeRecyclerView>(R.id.projectRecycler)
        projectRecycler.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        val adapter = ProjectItemAdapter(viewModel.projectList)
        adapter.onItemClickListener = object :OnItemClickListener<ProjectItem>{
            override fun onItemClicked(item: ProjectItem) {
                viewModel.project = item
                findNavController().navigate(R.id.action_mainFragment_to_projectEditFragment)
            }
        }
        projectRecycler.adapter = adapter

        projectRecycler.isItemViewSwipeEnabled = true
        projectRecycler.setOnItemMoveListener(
            object :OnItemMoveListener{
                override fun onItemMove(
                    srcHolder: RecyclerView.ViewHolder,
                    targetHolder: RecyclerView.ViewHolder
                ): Boolean {
                    val from = srcHolder.adapterPosition
                    val target = srcHolder.adapterPosition
                    Collections.swap(viewModel.projectList,from,target)
                    return true
                }

                override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {
                    val item = viewModel.projectList[srcHolder.adapterPosition]
                    viewModel.projectList.remove(item)
                    viewModel.deleteProject(item)
                    adapter.notifyItemRemoved(srcHolder.adapterPosition)
                }
            }
        )
        viewModel.projectLiveData.solve(viewLifecycleOwner){ result ->
            if ( result == viewModel.projectList ) return@solve
            adapter.notifyItemRangeRemoved(0,viewModel.projectList.size)
            viewModel.projectList.clear()
            viewModel.projectList.addAll(result)
            adapter.notifyItemRangeInserted(0,viewModel.projectList.size)
        }

        val insert = mainView.findViewById<ImageView>(R.id.insert)
        insert.setOnClickListener {
            viewModel.newProject()
            viewModel.saveProject()
            findNavController().navigate(R.id.action_mainFragment_to_projectEditFragment)
        }
    }

    private fun initHistory() {
        val imageRecycler = mainView.findViewById<RecyclerView>(R.id.imageRecycler)
        imageRecycler.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = ImageItemAdapter(requireActivity(),viewModel.historyList)
        adapter.onItemClickListener = object :OnItemClickListener<ImageItem>{
            override fun onItemClicked(item: ImageItem) {
                viewModel.newProject()
                viewModel.project.title = item.title
                viewModel.project.imageList.add(item.filePath)
                viewModel.saveProject()
                findNavController().navigate(R.id.action_mainFragment_to_projectEditFragment)
            }
        }
        imageRecycler.adapter = adapter
        viewModel.historyLiveData.solve(viewLifecycleOwner){ result ->
            if ( result == viewModel.historyList ) return@solve
            adapter.notifyItemRangeRemoved(0,viewModel.historyList.size)
            viewModel.historyList.clear()
            viewModel.historyList.addAll(result)
            adapter.notifyItemRangeInserted(0,viewModel.historyList.size)
        }
    }

}