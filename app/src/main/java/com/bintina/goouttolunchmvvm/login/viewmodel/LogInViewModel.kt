package com.bintina.goouttolunchmvvm.login.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.R
import com.facebook.login.LoginFragment


class LogInViewModel : ViewModel() {
    //var myName = name

    val logInFragment = LoginFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
    val mainContainerInt = R.id.main_fragment_container


    init{
        Log.d("ViewModelProviderLog","My youtube channel name is ")
    }

    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    }

}
