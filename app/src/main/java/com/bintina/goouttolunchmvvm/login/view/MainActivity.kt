package com.bintina.goouttolunchmvvm.login.view

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.login.viewmodel.LogInViewModel
import com.bintina.goouttolunchmvvm.login.viewmodel.LoginViewModelFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewModelFactory = LoginViewModelFactory()
        var viewModel = ViewModelProvider(this, viewModelFactory).get(LogInViewModel::class.java)

        Log.d("MainActivityLog", "Activity Main created")

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            viewModel.mainContainerInt,
            viewModel.logInFragment,
            viewModel.KEY_LOGIN_FRAGMENT
        )
        transaction.commit()
        Log.d("MainActivityLog", "Fragment committed")

    }

}