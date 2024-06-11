package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.app.Activity.RESULT_OK
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginViewModel(
    application: Application,
    //private val userId: Long,
    private val userDao: UserDao
) : ViewModel() {

    val TAG = "LoginViewModel"
val user: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun handleSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        Log.d(TAG, "handleSignInResult called")
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            this.user.value = user
            currentUser = user
        } else {
            // Handle error
            this.user.value = null
            response?.error?.let {
                Log.e(TAG, "Sign in error: ${it.errorCode}", it)
            } ?: run {
                Log.e(TAG, "Sign in canceled by user")
            }
        }
    }
}