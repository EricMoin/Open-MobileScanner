package com.mobilescanner.main.main.ui

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_login.data.adapter.PageAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        initTitleBar()
        initViewPager()
        initSettings()
        initFloatingButton()
    }

    private fun initSettings() {
        val navigation = mainView.findViewById<NavigationView>(R.id.navigation)
        navigation.setNavigationItemSelectedListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            true
        }
    }

    private fun initTitleBar() {
        val drawer = mainView.findViewById<DrawerLayout>(R.id.drawerLayout)
        val avatar = mainView.findViewById<ShapeableImageView>(R.id.avatar)
        avatar.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
        }
        val search = mainView.findViewById<TextView>(R.id.search)
        search.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        val message = mainView.findViewById<ImageView>(R.id.message)
        message.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_noticeFragment) }
        val settings = mainView.findViewById<ImageView>(R.id.settings)
        settings.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_settingsFragment) }
    }

    private fun initFloatingButton() {
        val scan = mainView.findViewById<ShapeableImageView>(R.id.scan)
        scan.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_captureFragment)
        }
    }

    private fun initViewPager() {
        val mainViewPager = mainView.findViewById<ViewPager2>(R.id.mainViewPager)
        mainViewPager.adapter = PageAdapter(childFragmentManager,lifecycle)
        mainViewPager.isUserInputEnabled = false
        val bottomNavigation = mainView.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener {
            mainViewPager.currentItem = PageAdapter.menuMap[it.itemId]!!
            true
        }
    }
}