package com.bintina.goouttolunchmvvm.restaurants.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant

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

    @Query("SELECT * FROM LocalRestaurant WHERE currentUserUid = :userUid")
    suspend fun getRestaurantsByUser(userUid: String): List<LocalRestaurant>

    @Query("SELECT * FROM LocalRestaurant")
    fun getAllRestaurants(): MutableList<LocalRestaurant>

    @Delete
    fun deleteRestaurant(restaurant: LocalRestaurant)

}