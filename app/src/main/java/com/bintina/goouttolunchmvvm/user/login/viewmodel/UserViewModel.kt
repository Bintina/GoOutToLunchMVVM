package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment
import java.util.concurrent.Executor


class UserViewModel(
    private val application: Application,
    private val userDataSource: UserDataRepository,
    private val userDao: UserDao,
    private val executor: Executor
) : AndroidViewModel(application) {


    private var currentUser: LiveData<User>? = null
    val vmLogInFragment = MyLogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_USER_LOGIN_FRAGMENT"
    val mainContainerInt = R.id.main_fragment_container
    val viewModel: ViewModel = this


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
    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        return currentUser
    }
}