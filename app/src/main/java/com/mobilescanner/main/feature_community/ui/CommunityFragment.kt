package com.mobilescanner.main.feature_community.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_community.data.adapter.ZoneViewPagerAdapter

class CommunityFragment : Fragment() {

    companion object {
        fun newInstance() = CommunityFragment()
    }

    private lateinit var viewModel: CommunityViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        initTabLayout()
    }
    private fun initTabLayout() {
        val zoneTabLayout = mainView.findViewById<TabLayout>(R.id.zoneTabLayout)
        val zoneViewPager = mainView.findViewById<ViewPager2>(R.id.zoneViewPager)
        val adapter = ZoneViewPagerAdapter(childFragmentManager,lifecycle)
        zoneViewPager.adapter = adapter
        TabLayoutMediator(zoneTabLayout,zoneViewPager,adapter.tabConfigurationStrategy).attach()
        zoneTabLayout.addOnTabSelectedListener(
            object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab){
                    zoneViewPager.currentItem = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) { }
                override fun onTabReselected(tab: TabLayout.Tab?) { }
            }
        )
    }
}