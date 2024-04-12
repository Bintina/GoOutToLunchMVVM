package com.bintina.goouttolunchmvvm.login.model.database

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.login.model.User

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUser(userId: Long): LiveData<User>
}