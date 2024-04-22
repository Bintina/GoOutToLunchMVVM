package com.bintina.goouttolunchmvvm.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.login.model.database.repositories.UserDataRepository
import java.util.concurrent.Executor

class UserViewModel (private val userDataSource: UserDataRepository, private val executor: Executor): ViewModel(){

    private var currentUser: LiveData<User>? = null

    fun init(userId: Long){
        if (currentUser != null){
            return
        }
        currentUser = userDataSource.getUser(userId)
    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        return currentUser
    }
}