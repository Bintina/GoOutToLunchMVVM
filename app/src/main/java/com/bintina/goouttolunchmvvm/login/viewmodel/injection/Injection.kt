package com.bintina.goouttolunchmvvm.login.viewmodel.injection

import android.content.Context
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.login.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.login.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.login.viewmodel.ViewModelFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideUserDataSource(context: Context): UserDataRepository {
        val database = SaveUserDatabase.getInstance(context)
        return UserDataRepository(database!!.userDao())
    }

    fun provideExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory{
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val application = MyApp()
        return ViewModelFactory(application, dataSourceUser, userDao, executor)
    }

    fun provideUserViewModel(context: Context): UserViewModel {
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val application = MyApp() // Assuming MyApp extends Application
        val factory = ViewModelFactory(application, dataSourceUser, userDao, executor)
        return factory.create(UserViewModel::class.java)
    }
}