package com.bintina.goouttolunchmvvm.restaurants.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM LocalRestaurant WHERE restaurantId = :restaurantId")
    fun getRestaurant(restaurantId: String): LiveData<LocalRestaurant>

    @Query("SELECT * FROM LocalRestaurant")
    fun getAllRestaurants(): MutableList<LocalRestaurant?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(restaurant: LocalRestaurant)

    @Delete
    fun deleteRestaurant(restaurant: LocalRestaurant)
}