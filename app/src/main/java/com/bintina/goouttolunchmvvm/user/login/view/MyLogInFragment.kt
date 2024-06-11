package com.bintina.goouttolunchmvvm.user.login.view//

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.navController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract


class MyLogInFragment : Fragment(), LifecycleOwner{

    private lateinit var viewModel: LoginViewModel

    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val TAG = "LoginFragLog"


    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        viewModel.handleSignInResult(result)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(LoginViewModel::class.java)

        viewModel.user.observe(viewLifecycleOwner, { user ->
                currentUser = user
            if (user != null) {
                navController.navigate(R.id.restaurant_list_dest)
            }
        })


        initializeViews()

        Log.d(TAG, "LoginFragment inflated")
        return binding.root

    }

    private fun initializeViews() {
        if (currentUser != null) {

            navController.navigate(R.id.restaurant_list_dest)
        } else {
            binding.facebookBtn.setOnClickListener{
                startFacebookSignIn()
            }
            binding.googleLoginBtn.setOnClickListener {
                startGoogleSignIn()
            }
        }
    }

    private fun startGoogleSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun startFacebookSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, "onDestroy called")
    }


}