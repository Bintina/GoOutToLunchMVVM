package com.bintina.goouttolunchmvvm.model.database.dao

import androidx.lifecycle.LiveData
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(restaurant: LocalRestaurant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    fun getRestaurantsWithUsers(): List<RestaurantWithUsers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserRestaurantCrossRef(crossRef: UserRestaurantCrossRef)

    @Delete
    fun deleteUserRestaurantCrossRef(crossRef: UserRestaurantCrossRef)

    @Transaction
    @Query("SELECT * FROM LocalRestaurant WHERE restaurantId = :restaurantId")
    fun getRestaurantWithUsers(restaurantId: String): RestaurantWithUsers

}