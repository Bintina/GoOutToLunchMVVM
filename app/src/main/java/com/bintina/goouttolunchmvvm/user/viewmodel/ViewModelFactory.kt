package com.bintina.goouttolunchmvvm.user.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao


class ViewModelFactory(
    val application: Application,
    /*private val userDataSource: UserDataRepository,
    private val executor: Executor*/
    //private val userId: Long,
    private val userDao: UserDao
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(application, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}