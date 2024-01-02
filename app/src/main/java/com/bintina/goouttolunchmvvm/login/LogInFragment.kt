package com.bintina.goouttolunchmvvm.login


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding

//@AndroidEntryPoint
class LogInFragment : Fragment() {
    private var _binding: FragmentLoginBinding? =null
    private val binding get() = _binding!!

   lateinit var viewModel: LogInViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val viewModelFactory = LoginViewModelFactory("Bintina")
        viewModel = ViewModelProvider(this, viewModelFactory).get(LogInViewModel::class.java)

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
        
        Log.d("LoginFragmentLog","initializeViews Called")
    }


}