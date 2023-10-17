package com.mobilescanner.main.feature_home.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.adapter.PlaceItemAdapter
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_home.remote.model.Place
import com.mobilescanner.main.main.data.utils.EricMoinUtils.solve

class SearchPlaceFragment : Fragment() {

    companion object {
        fun newInstance() = SearchPlaceFragment()
    }

    private lateinit var viewModel: SearchPlaceViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchPlaceViewModel::class.java]
        mainView = view
        initSearch()
        initRecycler()
    }

    private fun initRecycler() {
        val recycler = mainView.findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager( requireActivity() )
        val adapter = PlaceItemAdapter(viewModel.placeList)
        recycler.adapter = adapter
        adapter.onItemClickLister = object :OnItemClickListener<Place>{
            override fun onItemClicked(item: Place) {
                viewModel.savePlace(item)
                findNavController().popBackStack()
            }
        }
        viewModel.placeLiveData.solve(viewLifecycleOwner){ result ->
            result.onSuccess { data ->
                adapter.notifyItemRangeRemoved(0,viewModel.placeList.size)
                viewModel.placeList.clear()
                viewModel.placeList.addAll(data as List<Place>)
                adapter.notifyItemRangeInserted(0,viewModel.placeList.size)
            }
        }
    }
    private fun initSearch() {
        val searchBox = mainView.findViewById<EditText>(R.id.searchBox)
        val search = mainView.findViewById<Button>(R.id.search)
        search.setOnClickListener {
            val query = searchBox.text.toString()
            searchBox.text.clear()
            viewModel.searchPlace(query)
        }
    }

}