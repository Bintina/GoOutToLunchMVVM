package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository


class LoginViewModel(
   /* private val application: Application,
    ,*/
    //private val userId: Long,
    private val userDao: UserDao
) : ViewModel() {

    private var currentUser: LiveData<User>? = null
    private val userDataSource: UserDataRepository = UserDataRepository(userDao)

    // LiveData to trigger navigation
    private val _navigateToNextScreen = MutableLiveData<Boolean>()
    val navigateToNextScreen: LiveData<Boolean>
        get() = _navigateToNextScreen

    private var _userId: String? = null  // To store the userId
    val userId: String?
        get() = _userId


    fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }

    // For Facebook login
    fun handleFacebookLoginSuccess(userId: String) {
        _userId = userId   // Store the userId
        _navigateToNextScreen.value = true
    }

    // For Google login
    fun handleGoogleLoginSuccess(userId: String) {
        _userId = userId   // Store the userId
        _navigateToNextScreen.value = true
    }

    // Call this after navigation is complete to reset the LiveData
    fun doneNavigating() {
        _navigateToNextScreen.value = false
    }

//For Facebook login
    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    Log.d("LoginVMLog","setUserName called")
    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        Log.d("LoginVMLog", "User id is $userId")
        return currentUser
    }
}