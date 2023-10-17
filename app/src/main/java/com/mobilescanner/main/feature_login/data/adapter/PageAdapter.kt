package com.mobilescanner.main.feature_login.data.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_community.ui.CommunityFragment
import com.mobilescanner.main.feature_home.ui.HomeFragment
import com.mobilescanner.main.feature_project.ui.ProjectFragment
import com.mobilescanner.main.feature_tools.ToolsFragment

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount() = PAGE_COUNT
    override fun createFragment(position: Int) = fragmentList[position]

    companion object{
        const val POSITION = "position"
        const val PAGE_COUNT = 4
        val menuMap = mapOf(
            R.id.home to 0,
            R.id.project to 1,
            R.id.community to 2,
            R.id.tools to 3
        )
        val fragmentList = listOf(
            HomeFragment.newInstance(),
            ProjectFragment.newInstance(),
            CommunityFragment.newInstance(),
            ToolsFragment.newInstance()
        )
    }
}