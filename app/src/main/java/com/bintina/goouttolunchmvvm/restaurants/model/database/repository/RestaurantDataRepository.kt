package com.bintina.goouttolunchmvvm.restaurants.model.database.repository

import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao

class RestaurantDataRepository(private val restaurantDao: RestaurantDao) {

    fun getRestaurant(restaurantId: String): LocalRestaurant {
        return this.restaurantDao.getRestaurant(restaurantId)
    }

    fun getAllRestaurants(): MutableList<LocalRestaurant> {
        return restaurantDao.getAllRestaurants()
    }
}