package com.bintina.goouttolunchmvvm.user.coworkers.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

class ViewModelFactory (

    /*val application: Application,
    private val userDataSource: UserDataRepository,
*/
    private val userDao: UserDao
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CoworkersViewModel::class.java)) {
                CoworkersViewModel(userDao) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
