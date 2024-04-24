package com.bintina.goouttolunchmvvm.user.coworkers.viewmodel.injection

import android.content.Context
import com.bintina.goouttolunchmvvm.user.coworkers.viewmodel.CoworkersViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.login.viewmodel.ViewModelFactory
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideUserDataSource(context: Context): UserDataRepository {
        val database = SaveUserDatabase.getInstance(context)
        return UserDataRepository(database!!.userDao())
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val application = MyApp()
        return ViewModelFactory(application, dataSourceUser, userDao, executor)
    }

    fun provideCoworkerViewModel(context: Context): CoworkersViewModel {
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val application = MyApp() // Assuming MyApp extends Application
        val factory = ViewModelFactory(application, dataSourceUser, userDao, executor)
        return factory.create(CoworkersViewModel::class.java)
    }
}