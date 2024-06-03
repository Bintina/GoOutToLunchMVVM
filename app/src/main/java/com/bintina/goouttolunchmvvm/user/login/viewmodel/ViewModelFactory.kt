package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import java.util.concurrent.Executor


class ViewModelFactory(
    val application: Application,
    /*private val userDataSource: UserDataRepository,
    private val executor: Executor*/
    //private val userId: Long,
    private val userDao: UserDao
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application,userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}