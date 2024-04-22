package com.bintina.goouttolunchmvvm.login.viewmodel.injection

import android.content.Context
import com.bintina.goouttolunchmvvm.login.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.login.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.login.viewmodel.ViewModelFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideUserDataSource(context: Context): UserDataRepository {
        val database = SaveUserDatabase.getInstance(context)
        return UserDataRepository(database!!.userDao())
    }

    private fun provideExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory{
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceUser,executor)
    }
}