package com.bintina.goouttolunchmvvm.user.login.view//

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getRestaurantsWithUsersFromRealtimeDatabase
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.getInstance
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyLogInFragment : Fragment(), LifecycleOwner {

    private lateinit var viewModel: UserViewModel
    var coworkersList: List<LocalUser> = listOf()

    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val TAG = "LoginFragLog"
    private val _coworkers = MutableLiveData<List<LocalUser?>>()
    val coworkers: LiveData<List<LocalUser?>> get() = _coworkers

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            viewModel.handleSignInResult(result)
        }

    //Request notification Launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Go Out For Lunch can not post notifications without permission", Toast.LENGTH_LONG).show()
        }
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
getRestaurantsWithUsersFromRealtimeDatabase()
        //Set channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                ),
            )
        }
        val intent = requireActivity().intent
        intent.extras?.let {
            for (key in it.keySet()){

            val value = intent.extras?.getString(key)
            Log.d(TAG, "Key: $key, Value: $value")
            }
        }

        //loadCoworkers()


        binding.facebookBtn.setOnClickListener {
            Log.d(TAG, "facebook btn clicked")
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
            Log.d(TAG, "startFacebookSignIn called by onClick")
            subscribeToUpdates()
        }
        binding.googleLoginBtn.setOnClickListener {
            Log.d(TAG, "google btn clicked")
            startGoogleSignIn()
            subscribeToUpdates()
        }

        askNotificationPermission()
        addCoworker(viewModel.coworker)


        Log.d(TAG, "LoginFragment inflated")
        return binding.root

    }

    fun subscribeToUpdates(){
    Firebase.messaging.subscribeToTopic("User Choices")
        .addOnCompleteListener { task ->
            var msg = getString(R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = getString(R.string.msg_subscribe_failed)
            }
            Log.d(TAG,msg)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    logToken()
}

    private fun logToken() {
        Firebase.messaging.token.addOnCompleteListener(
            OnCompleteListener {task ->
                if (!task.isSuccessful){
                    Log.d(TAG, "Fetching FCM token failed", task.exception)
                    return@OnCompleteListener
                }
                //Get new token
                val token = task.result

                //Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        )

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

    fun addCoworker(localUser: LocalUser?) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (localUser != null) {
                Log.d(TAG, "addCoworker called with user ${localUser.displayName}")
                viewModel.userDao.insert(localUser)
            }
        }
    }


    private fun askNotificationPermission() {
        //Check API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Go Out For Lunch is allowed to post notifications", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
