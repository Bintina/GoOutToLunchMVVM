package com.bintina.goouttolunchmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.login.LogInViewModel
import com.bintina.goouttolunchmvvm.login.LoginViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewModelFactory = LoginViewModelFactory("Bintina")
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