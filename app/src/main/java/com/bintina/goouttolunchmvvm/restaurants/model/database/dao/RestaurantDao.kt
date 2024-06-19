package com.bintina.goouttolunchmvvm.restaurants.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bintina.goouttolunchmvvm.restaurants.model.RealtimeRestaurant

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM RealtimeRestaurant WHERE restaurantId = :restaurantId")
    fun getRestaurant(restaurantId: Long): LiveData<RealtimeRestaurant>
}