package com.mobilescanner.main.feature_community.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_community.ui.ContentFragment

class ZoneViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    private val tabTextList = listOf<String>(
        "推荐",
        "学习",
        "工作"
    )
    val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        tab.text = tabTextList[position]
    }
    override fun getItemCount() = PAGE_COUNT
    override fun createFragment(position: Int) = ContentFragment.newInstance()
    companion object{
        const val PAGE_COUNT = 3
    }
}