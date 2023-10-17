package com.mobilescanner.main.feature_home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.adapter.NoteItemAdapter
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.feature_home.data.listener.OnItemClickListener
import com.mobilescanner.main.feature_home.remote.model.Place
import com.mobilescanner.main.feature_home.remote.model.Weather
import com.mobilescanner.main.feature_home.remote.model.toSky
import com.mobilescanner.main.feature_home.ui.dialog.ProcessDialog
import com.mobilescanner.main.feature_home.ui.note.NoteEditViewModel
import com.mobilescanner.main.main.data.utils.Constants
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onFailed
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onSuccess
import com.mobilescanner.main.main.data.utils.EricMoinUtils.solve
import com.mobilescanner.main.main.data.utils.LocationUtils
import com.permissionx.guolindev.PermissionX
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener
import java.util.Collections

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var noteViewModel: NoteEditViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainView:View
    private lateinit var processDialog: ProcessDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.loadAllNote()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        noteViewModel = ViewModelProvider( requireActivity() )[NoteEditViewModel::class.java]
        homeViewModel = ViewModelProvider( requireActivity() )[HomeViewModel::class.java]
        processDialog = ProcessDialog()
        processDialog.show( childFragmentManager,"" )
        initPermissions()
        initRecycler()
        processDialog.dismiss()
    }

    private fun initPermissions() {
        PermissionX
            .init(this)
            .permissions(Constants.permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted){
                    initLayer()
                }
            }
    }
    private fun initLayer() {
        val locationText = mainView.findViewById<TextView>(R.id.locationText)
        locationText.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchPlaceFragment)
        }
        val weatherCard = mainView.findViewById<ConstraintLayout>(R.id.weatherCard)
        weatherCard.setOnClickListener {
            noteViewModel.noteItem = NoteItem( id= -1 )
            findNavController().navigate(R.id.action_mainFragment_to_noteEditFragment)
        }
        val location = if ( homeViewModel.isPlaceSaved() ) homeViewModel.getSavedPlace().location else {
            LocationUtils.getLocation( requireActivity() )
        }
        val place = if ( homeViewModel.isPlaceSaved() ) homeViewModel.getSavedPlace() else {
            Place("当前位置",location,"无详细地址")
        }
        homeViewModel.locationLng = location.lng
        homeViewModel.locationLat = location.lat
        homeViewModel.refreshWeather(homeViewModel.locationLng,homeViewModel.locationLat)
        if ( !homeViewModel.isPlaceSaved() ) homeViewModel.savePlace(place)
        homeViewModel.weatherLiveData.solve(viewLifecycleOwner){ result ->
            result.onSuccess { weather ->
                locationText.text = place.name
                homeViewModel.weather = weather as Weather
                showWeatherInfo(homeViewModel.weather!!)
            }.onFailed {
                Toast.makeText(activity,"无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }
    private fun showWeatherInfo(weather: Weather) {
        val realTime = weather.realtime
        val daily = weather.daily
        val degree = mainView.findViewById<TextView>(R.id.degree)
        degree.text = String.format("%.0f",realTime.temperature)
        val weatherIcon = mainView.findViewById<ImageView>(R.id.weatherIcon)
        Glide.with(this).load( daily.skycon[0].value.toSky().icon ).into(weatherIcon)
    }
    private fun initRecycler() {
        val noteRecycler = mainView.findViewById<SwipeRecyclerView>(R.id.noteRecycler)
//        noteRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteRecycler.layoutManager = LinearLayoutManager( requireActivity() )
        val adapter = NoteItemAdapter( noteViewModel.noteList )
        adapter.onItemClickListener = object :OnItemClickListener<NoteItem>{
            override fun onItemClicked(item: NoteItem) {
                noteViewModel.noteItem = item
                findNavController().navigate(R.id.action_mainFragment_to_noteEditFragment)
            }
        }
        noteRecycler.adapter = adapter
        noteRecycler.isItemViewSwipeEnabled = true
        noteRecycler.isLongPressDragEnabled = true
        noteRecycler.setOnItemMoveListener(
            object :OnItemMoveListener{
                override fun onItemMove(
                    srcHolder: RecyclerView.ViewHolder,
                    targetHolder: RecyclerView.ViewHolder
                ): Boolean {
                    val from = srcHolder.adapterPosition
                    val target = targetHolder.adapterPosition
                    Collections.swap(noteViewModel.noteList,from,target)
                    adapter.notifyItemMoved(from,target)
                    return true
                }
                override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {
                    val index = srcHolder.adapterPosition
                    noteViewModel.deleteNote(noteViewModel.noteList[index])
                    noteViewModel.noteList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }

            }
        )
        noteViewModel.noteLiveData.solve(viewLifecycleOwner){ result ->
            if ( result == noteViewModel.noteList ) return@solve
            adapter.notifyItemRangeRemoved(0,noteViewModel.noteList.size)
            noteViewModel.noteList.clear()
            noteViewModel.noteList.addAll(result)
            adapter.notifyItemRangeInserted(0,noteViewModel.noteList.size)
        }
    }

}