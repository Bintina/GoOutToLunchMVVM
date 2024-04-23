package com.bintina.goouttolunchmvvm.user.login.view//

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp


class MyLogInFragment : Fragment() {

    lateinit var viewModel: UserViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        viewModel = Injection.provideUserViewModel(MyApp.myContext)
        initializeViews()

        //viewModel.setUserName("Facebook Login")
        Log.d("LoginFragLog", "LoginFragment inflated")
        return binding.root

    }

    private fun initializeViews() {
        binding.facebookBtn.text = "Login with Facebook"
        binding.googleLogin.text = "Login with Google"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}