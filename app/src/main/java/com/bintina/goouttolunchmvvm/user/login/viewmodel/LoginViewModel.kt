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
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUser
import com.google.firebase.auth.FirebaseUser


class LoginViewModel(
   /* private val application: Application,
    ,*/
    //private val userId: Long,
    private val userDao: UserDao
) : ViewModel() {


    //public var currentUser: LiveData<User>? = null

    //TODO 1. Holding an instance for fragment and views here is not advised.


    val vmUserDao: UserDao = userDao
    private val userDataSource: UserDataRepository = UserDataRepository(userDao)


/*
    init {
        Log.d("UserViewModelLog","UserViewModel init block reached")
    }*/


   /* fun init(userId: Long) {
        if (MyApp.currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }*/
//For Facebook login
    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    Log.d("LoginVMLog","UserViewModel called")
    }

    //For User
    fun getUser(userId: Long): FirebaseUser? {
        Log.d("LoginVMLog", "User id is $userId")
        return currentUser
    }
}