package com.mobilescanner.main.feature_login.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_login.data.adapter.UserSettingsAdapter
import com.mobilescanner.main.feature_login.remote.model.toPair
import com.mobilescanner.main.feature_login.ui.LoginViewModel

class UserSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = UserSettingsFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var mainView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider( requireActivity() )[LoginViewModel::class.java]
        initSettings()
    }
    private fun initSettings() {
        val recycler = mainView.findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager( requireActivity() )
        recycler.adapter = UserSettingsAdapter(viewModel.registerUserInfo.toPair())
    }
}