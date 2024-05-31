package com.bintina.goouttolunchmvvm.user.login.view//

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.login.OnLogInOnClickListener
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.navController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth


class MyLogInFragment : Fragment(), LifecycleOwner, OnLogInOnClickListener {

    private lateinit var viewModel: LoginViewModel
    //private lateinit var context: Context

    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val TAG = "LoginFragLog"

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = Injection.provideUserViewModel(requireContext())
        viewModel.setUserName("Facebook Login")
        if (currentUser != null){

        navController.navigate(R.id.restaurant_list_dest)
        }

        Log.d(TAG, "LoginFragment inflated")
        return binding.root

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

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?): FirebaseAuthUIAuthenticationResult? {
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            currentUser = user
            navController.navigate(R.id.restaurant_list_dest)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Toast.makeText(requireContext(),"Your sign in was not successful. Please try again", Toast.LENGTH_LONG).show()
            response?.error?.let {
                Log.e(TAG, "Sign in error: ${it.errorCode}", it)
            } ?: run {
                Log.e(TAG, "Sign in canceled by user")
            }
        }
        return result
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, "onDestroy called")
    }

    override fun facebookClick() {
        startFacebookSignIn()
    }

    override fun googleClick() {
        startGoogleSignIn()
    }
}