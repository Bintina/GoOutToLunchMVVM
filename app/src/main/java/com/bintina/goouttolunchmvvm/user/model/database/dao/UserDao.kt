package com.bintina.goouttolunchmvvm.user.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.UserX
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // Insert a single UserX object
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    // Insert multiple UserX objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    // Get all UserX objects
    @Query("SELECT * FROM User")
    fun getAllUsers(): MutableList<User>

    // Get a specific UserX object by its localId
    @Query("SELECT * FROM User WHERE uid = :localId")
    fun getUser(localId: String): Flow<User>

    // ... any other methods you need
}