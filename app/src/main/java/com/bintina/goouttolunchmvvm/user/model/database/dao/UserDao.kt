package com.bintina.goouttolunchmvvm.user.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bintina.goouttolunchmvvm.user.model.LocalUser

@Dao
interface UserDao {
    // Insert a single UserX object
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(localUser: LocalUser)

    @Update
    suspend fun updateUser(localUser: LocalUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(localUsers: List<LocalUser>)

    // Get all UserX objects
    @Query("SELECT * FROM LocalUser")
    fun getAllUsers(): MutableList<LocalUser>

    // Get a specific UserX object by its localId
    @Query("SELECT * FROM LocalUser WHERE uid = :localId")
    fun getUser(localId: String): LocalUser

    @Query("SELECT * FROM LocalUser WHERE uid = :userId")
    suspend fun getUserById(userId: String): LocalUser

    @Delete
    suspend fun deleteUser(user: LocalUser)

}