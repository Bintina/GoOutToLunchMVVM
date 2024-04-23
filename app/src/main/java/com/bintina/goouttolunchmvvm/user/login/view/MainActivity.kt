package com.bintina.goouttolunchmvvm.user.login.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.injection.Injection
import java.util.concurrent.Executor


open class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        configureViewModel()


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

        viewModel = Injection.provideUserViewModel(MyApp.myContext)

    }

}