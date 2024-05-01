package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.app.Application
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
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(/*application,  userDataSource*/) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}