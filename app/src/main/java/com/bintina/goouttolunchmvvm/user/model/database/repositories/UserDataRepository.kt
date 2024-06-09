package com.bintina.goouttolunchmvvm.user.model.database.repositories

import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.UserX
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import kotlinx.coroutines.flow.Flow

class UserDataRepository(private val userDao: UserDao) {

    //Get User
    fun getUser(userId: String): Flow<UserX> {
        return this.userDao.getUser(userId)
    }
}