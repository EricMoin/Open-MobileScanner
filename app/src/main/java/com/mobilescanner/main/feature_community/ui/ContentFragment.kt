package com.mobilescanner.main.feature_community.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_community.data.adapter.ContentItemAdapter
import com.mobilescanner.main.feature_community.data.item.ContentItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener

class ContentFragment : Fragment() {

    companion object {
        fun newInstance() = ContentFragment()
    }

    private lateinit var contentViewModel: ContentViewModel
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var mainView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        contentViewModel = ViewModelProvider( requireActivity() )[ContentViewModel::class.java]
        initRecycler()
    }

    private fun initRecycler() {
        val contentRecycler = mainView.findViewById<RecyclerView>(R.id.contentRecycler)
        contentRecycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val adapter = ContentItemAdapter( this,contentViewModel.contentList)
        contentRecycler.adapter = adapter
        adapter.onItemClickListener = object :OnItemClickListener<ContentItem>{
            override fun onItemClicked(item: ContentItem) {
                findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
            }
        }
    }
}