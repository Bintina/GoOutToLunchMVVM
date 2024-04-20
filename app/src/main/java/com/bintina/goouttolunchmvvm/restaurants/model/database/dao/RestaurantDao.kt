package com.bintina.goouttolunchmvvm.restaurants.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM Restaurant ")
    fun getRestaurant(userId: Long): LiveData<Restaurant>
}