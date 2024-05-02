package com.bintina.goouttolunchmvvm.restaurants.model.database.repository

import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao

class RestaurantDataRepository(private val restaurantDao: RestaurantDao) {

    fun getRestaurant(restaurantId: Long): LiveData<Restaurant> {
        return this.restaurantDao.getRestaurant(restaurantId)
    }
}