package com.bintina.goouttolunchmvvm.user.viewmodel.injection

import android.content.Context
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.ViewModelFactory
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideUserDataSource(context: Context): UserDao {
        val database = SaveUserDatabase.getInstance(context)
        return database.userDao()
    }

    fun provideExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        /*val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()*/
        val userDao = provideUserDataSource(context)
        val application = MyApp()
        return ViewModelFactory(application, userDao)
    }

    fun provideUserViewModel(context: Context): UserViewModel {
        /*val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()*/
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val application = MyApp() // Assuming MyApp extends Application
        val factory = ViewModelFactory(application,userDao)
        return factory.create(UserViewModel::class.java)
    }


}