package com.bintina.goouttolunchmvvm.user.model.database.repositories

import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: Long): LiveData<User> {
        return this.userDao.getUser(userId)
    }
}