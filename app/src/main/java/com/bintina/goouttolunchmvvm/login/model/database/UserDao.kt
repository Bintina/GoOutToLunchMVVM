package com.bintina.goouttolunchmvvm.login.model.database


import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bintina.goouttolunchmvvm.login.model.User

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUser(userId: Long): LiveData<User>
}