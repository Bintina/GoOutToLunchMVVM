package com.bintina.goouttolunchmvvm.user.login.view//

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class MyLogInFragment : Fragment(), LifecycleOwner, OnLogInOnClickListener {

    private lateinit var viewModel: LoginViewModel
    //private lateinit var context: Context

    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController
    private val TAG = "LoginFragLog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = Injection.provideUserViewModel(requireContext())
        viewModel.setUserName("Facebook Login")

        initializeViews()
        Log.d(TAG, "LoginFragment inflated")
        return binding.root

    }

    /*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


            //val argsUserName = safeArgs.userName
            var argsUserName = safeArgs.userName

            Log.d("MyLoginFragLog","safe args userName value is $argsUserName")
            // Create a new Bundle and set the new value
            val newArgs = Bundle().apply {
                putString("userName", "Belladona")
            }
            Log.d("MyLoginFragLog","newArgs value is $newArgs")
            val newArgValue = safeArgs.userName
            Log.d("MyLoginFragLog","new safe args userName value is $newArgValue")



            Log.d("LoginFragLog", "LoginFragment inflated")
        }*/

    private fun initializeViews() {
        if (signedIn() == true) {
            navController.navigate(R.id.restaurant_list_dest)
        } else {
            initializeSignInOptions()
        }
    Log.d(TAG, "initializeViews called")
    }

    private fun initializeSignInOptions() {
        binding.facebookBtn.setOnClickListener { facebookClick() }

        binding.googleLoginBtn.setOnClickListener { googleClick() }
        Log.d(TAG,"initializeSignInOptions Called")
    }

    private fun signedIn(): Boolean {
        return if (MyApp.currentUser?.loggedInWithFacebook == false && MyApp.currentUser?.loggedInWithGmail == false) {
            false
        } else {
            true
        }
        Log.d(TAG, "signedIn Boolean method called")
    }

    override fun facebookClick() {

        Log.d(TAG, "override facebookClick called")
    }

    override fun googleClick() {

        Log.d(TAG, "override googleClick called")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, "onDestroy called")
    }
}