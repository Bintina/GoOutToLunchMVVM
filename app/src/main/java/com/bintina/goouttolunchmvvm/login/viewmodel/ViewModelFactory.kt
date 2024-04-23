package com.bintina.goouttolunchmvvm.login.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.MyApp
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.login.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.login.model.database.repositories.UserDataRepository
import java.util.concurrent.Executor
import javax.sql.DataSource


class ViewModelFactory(
    val application: Application,
    private val userDataSource: UserDataRepository,
    private val userDao: UserDao,
    private val executor: Executor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(application, userDataSource, userDao, executor) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}