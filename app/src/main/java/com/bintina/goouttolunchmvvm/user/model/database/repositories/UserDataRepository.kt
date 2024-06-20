package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import kotlinx.coroutines.flow.Flow

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: String): Flow<LocalUser> {
        return this.userDao.getUser(userId)
    }
}