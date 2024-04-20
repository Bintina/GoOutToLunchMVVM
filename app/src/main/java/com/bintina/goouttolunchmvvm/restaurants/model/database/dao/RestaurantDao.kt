package com.bintina.goouttolunchmvvm.restaurants.model.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM Restaurant ")
    fun getRestaurant(userId: Long): LiveData<Restaurant>
}