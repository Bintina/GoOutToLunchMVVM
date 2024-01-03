package com.bintina.goouttolunchmvvm.login


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment(), OnLogInOnClickListener {
    private val viewModel: LogInViewModel by viewModels()
    private var _binding: FragmentLoginBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        initializeView()
     return binding.root
    }
    override fun onResume() {
        Log.d("LoginOnResumeLog", "LoginFragment onResume called")
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeView() {
        binding.facebookLogin.setOnClickListener { facebookClick() }

        binding.googleLogin.setOnClickListener { googleClick() }
        Log.d("LoginFragmentLog","initializeViews Called")
    }

    override fun facebookClick() {
        TODO("Not yet implemented")
    }

    override fun googleClick() {
        TODO("Not yet implemented")
    }


}