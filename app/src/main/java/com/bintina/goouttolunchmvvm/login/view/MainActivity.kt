package com.bintina.goouttolunchmvvm.login.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.login.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.login.viewmodel.injection.Injection
import java.util.concurrent.Executor


open class MainActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var executor: Executor
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewModelFactory = Injection.provideViewModelFactory(MyApp.myContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        userDao = viewModel.vmUserDao
        executor = viewModel.vmExecutor

        Log.d("MainActivityLog", "Activity Main created")

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            viewModel.mainContainerInt,
            viewModel.vmLogInFragment,
            viewModel.KEY_LOGIN_FRAGMENT
        )
        Log.d("MainActLog", "viewModel value is ${viewModel.vmLogInFragment}")
        transaction.commit()
        Log.d("MainActivityLog", "Fragment committed")
    }

    private fun configureViewModel() {

    }

}