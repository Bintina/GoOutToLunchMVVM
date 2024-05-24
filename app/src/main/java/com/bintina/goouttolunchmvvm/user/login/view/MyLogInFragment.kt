package com.bintina.goouttolunchmvvm.user.login.view//

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MyLogInFragment : Fragment(), LifecycleOwner {

    private lateinit var viewModel: LoginViewModel


    //private val safeArgs: MyLogInFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //Google vals
    private val TAG = "GoogleSignInActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = Injection.provideUserViewModel(requireContext())
        viewModel.setUserName("Facebook Login")

        initializeViews()
        //Google Sign in
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id_google))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso) // Use requireActivity()

        binding.googleLoginBtn.setOnClickListener {
            Log.d("LoginFragLog", "Google sign in btn clicked")
            signIn()
            Toast.makeText(
                requireContext(),
                "Signed in as ${viewModel.userId.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Observe the LiveData for navigation
        viewModel.navigateToNextScreen.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                // User has logged in successfully - navigate to the next screen
                val userId =
                    viewModel.userId ?: ""
                val action = MyLogInFragmentDirections.goToRestaurantsList(userId)
                findNavController().navigate(action)

                viewModel.doneNavigating() // Reset the LiveData
            }

            Log.d("LoginFragLog", "LoginFragment inflated")

        }
        return binding.root
    }


    private fun initializeViews() {
        binding.facebookBtn.text = "Login with Facebook"
        Log.d("LoginFragLog", "initializeViews called")

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("LoginFragLog", "onDestroy called")
    }

    // Launcher for Google Sign-In
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("LoginFragLog", "googleSignInLauncher called")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("LoginFragLog", "result is ${result.resultCode}")
                val data: Intent? = result.data
                // Handle the sign-in result
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account?.idToken)
                    Log.d("LoginFragLog", "googleSignInMethod reached and succeeded")
                } catch (e: ApiException) {
                    Log.d("LoginFragLog", "Google sign in failed", e)
                }
            } else {
                // Handle different result codes
                when (result.resultCode) {
                    Activity.RESULT_CANCELED -> {
                        Log.d("LoginFragLog", "Google Sign-In canceled")
                        // Show a message to the user indicating that they canceled
                        Toast.makeText(
                            requireContext(),
                            "Google Sign-In canceled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Log.d(
                            "LoginFragLog",
                            "Google Sign-In failed with resultCode: ${result.resultCode}"
                        )
                        // Handle other error cases (if any)
                        // For example, you might show a generic error message
                        Toast.makeText(
                            requireContext(),
                            "Google Sign-In failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent) // Use the launcher
        Log.d("LoginFragLog", "signIn method called")
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        Log.d("LoginFragLog", "firebaseAuthWithGoogle called")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginFragLog", "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        requireContext(),
                        "Signed in as ${user?.email}",
                        Toast.LENGTH_SHORT
                    ).show()// Pass the ID token to Firebase
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }


    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(
            requireContext(),
            "Signed in as ${user?.email}",
            Toast.LENGTH_SHORT
        ).show()
        // Get the user ID (or an empty string if null)
        val userId = user?.uid ?: ""

        // Create the navigation action using Safe Args
        val action = MyLogInFragmentDirections.goToRestaurantsList(userId)

        Log.d("LoginFragLog", "updateUi method reached")
        // Navigate
        findNavController().navigate(action)

        if (user != null) {
            // Assuming you'll fetch the userId associated with the Google account
            val userId = // ... your logic to get userId from FirebaseUser ...

                viewModel.handleGoogleLoginSuccess(userId)
        } else {
            // Handle login failure
            Toast.makeText(requireContext(), "Google Sign-In failed.", Toast.LENGTH_SHORT)
                .show()
        }
    }

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
