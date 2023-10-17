package com.mobilescanner.main.feature_tools

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mobilescanner.main.R

class ToolsFragment : Fragment() {

    companion object {
        fun newInstance() = ToolsFragment()
    }

    private lateinit var viewModel: ToolsViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tools, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[ToolsViewModel::class.java]
        val fillBackground = mainView.findViewById<ImageView>(R.id.fillBackground)
        Glide.with(this).load(R.drawable.circle).into(fillBackground)
    }
}