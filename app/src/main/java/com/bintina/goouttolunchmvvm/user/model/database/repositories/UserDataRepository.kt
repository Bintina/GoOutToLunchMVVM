package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.database.dao.UserDao

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: String): LocalUser? {
        return this.userDao.getUser(userId)
    }

    fun getUserList(): List<LocalUser?> {
        return userDao.getAllUsers()
    }
}