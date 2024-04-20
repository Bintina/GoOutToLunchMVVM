package com.bintina.goouttolunchmvvm.restaurants.model.database


import androidx.room.Insert
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurant: Restaurant)


}