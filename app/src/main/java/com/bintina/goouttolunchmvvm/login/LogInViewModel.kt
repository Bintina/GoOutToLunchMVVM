package com.bintina.goouttolunchmvvm.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class LogInViewModel : ViewModel() {


    val logInFragment = LogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"


    private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    }
}