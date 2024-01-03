package com.bintina.goouttolunchmvvm.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//
@HiltViewModel
class LogInViewModel @Inject constructor(name: String) : ViewModel() {
    var myName = name

    val logInFragment = LogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
    val mainContainerInt = R.id.main_fragment_container


    init{
        Log.d("ViewModelProviderLog","My youtube channel name is $myName")
    }

   /* private val _facebookLoginBtn = MutableLiveData<String>()
    val facebookLoginBtn: LiveData<String>
        get() = _facebookLoginBtn

    fun setUserName(facebookLoginBtn: String) {
        _facebookLoginBtn.value = facebookLoginBtn
    }*/
}