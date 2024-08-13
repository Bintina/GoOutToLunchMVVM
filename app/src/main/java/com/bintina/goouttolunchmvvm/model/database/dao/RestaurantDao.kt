package com.bintina.goouttolunchmvvm.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertRestaurant(restaurant: LocalRestaurant)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    abstract fun insertAll(listRestaurants: List<LocalRestaurant>)

    @Update
    suspend fun updateRestaurant(restaurant: LocalRestaurant)

    @Query("SELECT * FROM LocalRestaurant WHERE restaurantId = :restaurantId")
    fun getRestaurant(restaurantId: String): LocalRestaurant

    @Query("SELECT * FROM LocalRestaurant WHERE name = :restaurantName")
    fun getRestaurantByName(restaurantName: String): LocalRestaurant

   /* @Query("SELECT * FROM LocalRestaurant WHERE currentUserUid = :userUid")
    suspend fun getRestaurantsByUser(userUid: String): List<LocalRestaurant>
*/
    @Query("SELECT * FROM LocalRestaurant")
    fun getAllRestaurants(): MutableList<LocalRestaurant>

    @Delete
    fun deleteRestaurant(restaurant: LocalRestaurant)

    @Transaction
    @Query("SELECT * FROM LocalRestaurant")
    suspend fun getRestaurantsWithUsers(): List<RestaurantWithUsers>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUserRestaurantCrossRef(crossRef: UserRestaurantCrossRef)

    @Delete
    suspend fun deleteUserRestaurantCrossRef(crossRef: UserRestaurantCrossRef)

    @Transaction
    @Query("SELECT * FROM LocalRestaurant WHERE restaurantId = :restaurantId")
    suspend fun getRestaurantWithUsers(restaurantId: String): RestaurantWithUsers

}