package com.bintina.goouttolunchmvvm.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
   @Inject lateinit var viewModel: LogInViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        viewModel.setUserName("Facebook Login")

        return super.onCreateView(inflater, container, savedInstanceState)

    }
}