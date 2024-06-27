package com.bintina.goouttolunchmvvm.user.viewmodel

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.bintina.goouttolunchmvvm.utils.mapFirebaseUserToUser
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel(
    application: Application,
    //private val userId: Long,
    val userDao: UserDao
) : ViewModel() {

    val TAG = "UserVMLog"
    val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    var coworker: LocalUser? = null
    var coworkerList: MutableLiveData<List<LocalUser?>> = MutableLiveData()
    val callbackManager = CallbackManager.Factory.create()
    lateinit var databaseReference: DatabaseReference

    fun handleSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        Log.d(TAG, "handleSignInResult called")
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            this.user.value = user
            saveUserToDatabase(user)
            currentUser = user

            MyApp.navController.navigate(R.id.restaurant_list_dest)
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

    init {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "Facebook login canceled.")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "Facebook login failed: ${error.message}")
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    this.user.value = user
                    currentUser = user
                    saveUserToDatabase(user)
                    MyApp.navController.navigate(R.id.restaurant_list_dest)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUserToDatabase(firebaseUser: FirebaseUser?) {


        firebaseUser?.let {
            val localUser = mapFirebaseUserToUser(it)


            viewModelScope.launch(Dispatchers.IO) {
                userDao.insert(localUser)
                writeToDatabase(localUser)
            }
            //TODO("create check for users already in database.")
        }
    }

    fun getCoworkers(context: Context): MutableLiveData<List<LocalUser?>> {

        viewModelScope.launch(Dispatchers.IO) {
            val result: MutableList<LocalUser?> = try {
                userDao.getAllUsers()
            } catch (e: Exception) {
                Log.d(TAG, "Error is $e. Cause is ${e.cause}")
                mutableListOf()
            }
            if (result.isEmpty()) {
                Log.d(TAG, "CoworkerListFragment result is empty")
            } else {
                withContext(Dispatchers.Main) {
                    coworkerList.postValue(result)
                    Log.d(
                        TAG,
                        "CoworkerListFragment result has ${result.size} items"
                    )
                }
            }

        }
        return coworkerList
        /*var list: MutableList<User?> = mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            list = userDao.getAllUsers()
        }
        return list*/
    }
    private fun writeToDatabase(localUser: LocalUser) {
        databaseReference = Firebase.database.reference
        //Writing data to Firebase Realtime Database
        val firebaseUserId = databaseReference.push().key!!

        databaseReference.child("users").child(firebaseUserId).setValue(localUser)
            .addOnCanceledListener {
                Log.d(TAG, "Write to database canceled")
            }
            .addOnFailureListener{
                Log.d(TAG, "Write to database failed")
            }

    }
}