package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import java.util.concurrent.Executor


class ViewModelFactory(
    val application: Application,
    private val userDataSource: UserDataRepository,
    private val userDao: UserDao,
    private val executor: Executor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(application,  userDataSource) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}