package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: String): LocalUser {
        return this.userDao.getUser(userId)
    }

    fun getUserList(): MutableList<LocalUser> {
        return userDao.getAllUsers()
    }
}