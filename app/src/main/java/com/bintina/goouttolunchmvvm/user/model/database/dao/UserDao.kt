package com.bintina.goouttolunchmvvm.user.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.UserX
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // Insert a single UserX object
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserX)

    // Insert multiple UserX objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserX>)

    // Get all UserX objects
    @Query("SELECT * FROM UserX")
    fun getAllUsers(): Flow<List<UserX>>

    // Get a specific UserX object by its localId
    @Query("SELECT * FROM UserX WHERE localId = :localId")
    fun getUser(localId: String): Flow<UserX>

    // ... any other methods you need
}