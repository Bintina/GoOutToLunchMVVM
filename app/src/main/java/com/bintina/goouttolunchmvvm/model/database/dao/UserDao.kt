package com.bintina.goouttolunchmvvm.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef
import com.bintina.goouttolunchmvvm.model.UserWithRestaurant


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
    fun getAllUsers(): List<LocalUser>

    // Get a specific UserX object by its localId
    @Query("SELECT * FROM LocalUser WHERE uid = :uid")
    fun getUser(uid: String): LocalUser?

    @Delete
    suspend fun deleteUser(user: LocalUser)

    @Delete
    suspend fun deleteRestaurantUserCrossRef(crossRef: UserRestaurantCrossRef)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRestaurantUserCrossRef(crossRef: UserRestaurantCrossRef)

 /*   @Transaction
    @Query("SELECT * FROM UserRestaurantCrossRef WHERE uid = :uid")
    suspend fun getUserWithRestaurant(uid: String): UserWithRestaurant
*/

    @Transaction
    @Query("SELECT * FROM LocalUser")
    suspend fun getUsersWithRestaurants(): List<UserWithRestaurant>
    @Transaction
    @Query(" SELECT u.* FROM LocalUser u INNER JOIN UserRestaurantCrossRef urc ON u.uid = urc.uid WHERE urc.restaurantId = :restaurantId")
    fun getUsersForRestaurant(restaurantId: String): List<LocalUser>

}