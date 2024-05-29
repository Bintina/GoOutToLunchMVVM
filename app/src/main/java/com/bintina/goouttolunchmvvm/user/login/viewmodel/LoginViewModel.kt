package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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


    public var currentUser: LiveData<User>? = null

    //TODO 1. Holding an instance for fragment and views here is not advised.

    val KEY_LOGIN_FRAGMENT = "KEY_USER_LOGIN_FRAGMENT"
    //val mainContainerInt = R.id.main_fragment_container

    val vmUserDao: UserDao = userDao
    private val userDataSource: UserDataRepository = UserDataRepository(userDao)


/*
    init {
        Log.d("UserViewModelLog","UserViewModel init block reached")
    }*/


    fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }
//For Facebook login
    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    Log.d("LoginVMLog","UserViewModel called")
    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        Log.d("LoginVMLog", "User id is $userId")
        return currentUser
    }
}