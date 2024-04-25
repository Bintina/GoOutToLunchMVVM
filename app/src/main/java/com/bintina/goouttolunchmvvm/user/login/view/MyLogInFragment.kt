package com.bintina.goouttolunchmvvm.user.login.view//

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.ViewModelFactory
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp


class MyLogInFragment : Fragment() {

    lateinit var viewModel: UserViewModel
    private lateinit var context: Context

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //viewModel.setUserName("Facebook Login")
        Log.d("LoginFragLog", "LoginFragment inflated")
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize viewModel here
        context = ApplicationProvider.getApplicationContext()
        val viewModelFactory = Injection.provideViewModelFactory(context)
        viewModel = viewModelFactory.create(UserViewModel::class.java)

        //viewModel = Injection.provideUserViewModel(context)

        initializeViews()

        Log.d("LoginFragLog", "LoginFragment inflated")
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