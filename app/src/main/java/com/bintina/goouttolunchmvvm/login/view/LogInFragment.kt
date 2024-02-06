package com.bintina.goouttolunchmvvm.login.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
<<<<<<< Updated upstream:app/src/main/java/com/bintina/goouttolunchmvvm/login/LogInFragment.kt
import dagger.hilt.android.AndroidEntryPoint
=======
import com.bintina.goouttolunchmvvm.login.viewmodel.LogInViewModel
import com.bintina.goouttolunchmvvm.login.viewmodel.LoginViewModelFactory

>>>>>>> Stashed changes:app/src/main/java/com/bintina/goouttolunchmvvm/login/view/LogInFragment.kt

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

<<<<<<< Updated upstream:app/src/main/java/com/bintina/goouttolunchmvvm/login/LogInFragment.kt
    override fun facebookClick() {
        TODO("Not yet implemented")
    }

    override fun googleClick() {
        TODO("Not yet implemented")
    }


=======
>>>>>>> Stashed changes:app/src/main/java/com/bintina/goouttolunchmvvm/login/view/LogInFragment.kt
}