package com.mobilescanner.main.feature_login.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mobilescanner.main.main.ui.MainActivity
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_login.remote.model.LoginBody
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onFailed
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onSuccess
import com.mobilescanner.main.main.data.utils.EricMoinUtils.solve
import com.mobilescanner.main.main.data.utils.EricMoinUtils.start
import com.mobilescanner.main.main.data.utils.EricMoinUtils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var mainView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        initLogin()
    }
    private fun initLogin() {
        val account = mainView.findViewById<EditText>(R.id.account)
        val password = mainView.findViewById<EditText>(R.id.password)
        val login = mainView.findViewById<TextView>(R.id.login)
        login.setOnClickListener {
//            viewModel.loginUserInfo = LoginBody(account.text.toString(),password.text.toString())
//            viewModel.login()
            findNavController().popBackStack()
            findNavController().navigate(R.id.mainFragment)
        }
        viewModel.loginLiveData.solve(viewLifecycleOwner){ result ->
            result.onSuccess { data ->
                data?.data?.let { viewModel.jwtToken = it }
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }.onFailed {
                toast( "网络错误" )
            }
        }
        val register = mainView.findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}