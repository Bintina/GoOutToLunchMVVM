package com.bintina.goouttolunchmvvm.login.model.database.repositories

import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.login.model.database.dao.UserDao

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: Long): LiveData<User> {
        return this.userDao.getUser(userId)
    }
}