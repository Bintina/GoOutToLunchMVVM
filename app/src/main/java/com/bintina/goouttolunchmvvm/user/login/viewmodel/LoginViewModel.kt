package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment


class LoginViewModel(
   /* private val application: Application,
    private val userDataSource: UserDataRepository,*/

) : ViewModel() {


    private var currentUser: LiveData<User>? = null
    val vmLogInFragment = MyLogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_USER_LOGIN_FRAGMENT"
    //val mainContainerInt = R.id.main_fragment_container
    val viewModel: ViewModel = this


/*
    init {
        Log.d("UserViewModelLog","UserViewModel init block reached")
    }*/


    fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
       // currentUser = userDataSource.getUser(userId)

    }
//For Facebook login
    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    Log.d("UserViewModelLog","UserViewModel called")
    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        return currentUser
    }
}