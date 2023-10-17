package com.mobilescanner.main.feature_login.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_login.data.adapter.UserSettingsAdapter
import com.mobilescanner.main.feature_login.ui.LoginViewModel

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        mainView = view
        initSettings()
    }
    private fun initSettings() {
        val avatar = mainView.findViewById<ImageView>(R.id.avatar)
        avatar.setOnClickListener { findNavController().navigate(R.id.action_settingsFragment_to_userSettingsFragment) }
    }
}