package com.bintina.goouttolunchmvvm.restaurants.model.database.repository

import androidx.lifecycle.LiveData
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao

class RestaurantDataRepository(private val restaurantDao: RestaurantDao) {

    fun getRestaurant(restaurantId: String): LiveData<LocalRestaurant> {
        return this.restaurantDao.getRestaurant(restaurantId)
    }
}