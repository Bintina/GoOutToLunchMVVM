package com.bintina.goouttolunchmvvm.user.login.view//

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI.*
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyLogInFragment : Fragment(), LifecycleOwner {

    private lateinit var viewModel: UserViewModel

    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val TAG = "LoginFragLog"


    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            viewModel.handleSignInResult(result)
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
                UserViewModel::class.java
            )

        viewModel.user.observe(viewLifecycleOwner) { user ->
            currentUser = user
            Log.d(TAG, "onCreateView currentUser name is ${currentUser?.displayName}")
        }


            binding.facebookBtn.setOnClickListener {
                Log.d(TAG, "facebook btn clicked")
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
                Log.d(TAG, "startFacebookSignIn called by onClick")
            }
            binding.googleLoginBtn.setOnClickListener {
                Log.d(TAG, "google btn clicked")
                startGoogleSignIn()
            }

        addCoworker(viewModel.coworker)
        Log.d(TAG, "LoginFragment inflated")
        return binding.root

    }
    @Deprecated("This method is deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun startGoogleSignIn() {
        val providers = arrayListOf(
            IdpConfig.GoogleBuilder().build()
        )
        Log.d(TAG, "startGoogleSignIn called")
        val signInIntent = getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun startFacebookSignIn() {
        val providers = arrayListOf(
            com.firebase.ui.auth.AuthUI.IdpConfig.FacebookBuilder().build()
        )
        Log.d(TAG, "startFacebookSignIn called")

        val signInIntent = getInstance()
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

    fun addCoworker(user: User?) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (user != null) {
                Log.d(TAG, "addCoworker called with user ${user.displayName}")
            viewModel.userDao.insert(user)
            }
        }
    }

}