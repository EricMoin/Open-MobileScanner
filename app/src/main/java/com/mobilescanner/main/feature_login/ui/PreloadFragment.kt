package com.mobilescanner.main.feature_login.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobilescanner.main.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreloadFragment : Fragment() {

    companion object {
        fun newInstance() = PreloadFragment()
    }

    private lateinit var viewModel: PreloadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(2000)
            findNavController().popBackStack()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}