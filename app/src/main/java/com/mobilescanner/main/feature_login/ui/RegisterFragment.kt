package com.mobilescanner.main.feature_login.ui

import android.app.Activity.RESULT_OK
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobilescanner.main.R
import com.mobilescanner.main.main.data.utils.EricMoinUtils.onFailed
import com.mobilescanner.main.main.data.utils.EricMoinUtils.toast
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }
    private lateinit var mainView:View
    private lateinit var viewModel:LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)
    
    private val launcher = FileUtils.forResult(this){ uri ->
        val filePath = FileUtils.getFilePathFromUri(requireActivity(),uri)
        val avatarIcon = mainView.findViewById<ImageView>(R.id.avatarIcon)
        Glide.with(this).load(filePath).into(avatarIcon)
        viewModel.registerUserInfo.image = filePath!!
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val avatarIcon = mainView.findViewById<ImageView>(R.id.avatarIcon)
        avatarIcon.setOnClickListener { FileUtils.callAlbum(launcher) }
        val usernameInput = mainView.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = mainView.findViewById<EditText>(R.id.passwordInput)
        val emailInput = mainView.findViewById<EditText>(R.id.emailInput)
        val phoneInput = mainView.findViewById<EditText>(R.id.phoneInput)
        val register = mainView.findViewById<Button>(R.id.register)
        register.setOnClickListener {
            viewModel.registerUserInfo.username = usernameInput.text.toString()
            viewModel.registerUserInfo.password = passwordInput.text.toString()
            val phone = phoneInput.text.toString()
            viewModel.registerUserInfo.phone =
                if ( phone.isNotEmpty() ) phone.toLong() else 0L
            viewModel.register()
        }
        viewModel.registerLiveData.observe(
            viewLifecycleOwner, Observer { result ->
                result.onSuccess {
                    "注册成功".logD("Register")
                    toast("注册成功")
                    findNavController().popBackStack()
                }.onFailed {
                    toast("注册失败")
                }
            }
        )
        viewModel.loginLiveData.observe(
            viewLifecycleOwner, Observer { result ->
                result.getOrNull()?.data?.let{ it ->
                    "Token ${it}".logD("Register")
                    viewModel.jwtToken = it
                }
            }
        )
    }
}