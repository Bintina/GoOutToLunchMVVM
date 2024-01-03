package com.bintina.goouttolunchmvvm.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding


//
// @HiltViewModel
class LogInViewModel(name: String) : ViewModel() {
    var myName = name


    val logInFragment = LogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
val image: Int = R.drawable.go4lunch4

    init{
        Log.d("ViewModelProvider","My youtube channel name is $myName")
    }

   /* private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    }*/
}