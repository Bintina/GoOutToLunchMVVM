package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository


class RestaurantViewModel(
    private val userDao: UserDao,
    private val restaurantDao: RestaurantDao
): ViewModel(){

    private var currentRestaurant: LiveData<Restaurant>? = null

    private val userDataSource: UserDataRepository = UserDataRepository(userDao)
    private val restaurantDataSource: RestaurantDataRepository = RestaurantDataRepository(restaurantDao)
   /* fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }*/
    fun initRestaurant(restaurantId: Long) {
        if (currentRestaurant != null) {
            return
        }
        currentRestaurant = restaurantDataSource.getRestaurant(restaurantId)

    }

    fun getRestaurant(restaurantId: Long): LiveData<Restaurant>? {
        Log.d("RestMapInjectLog", "restaurant id is $restaurantId")
        return currentRestaurant
    }

}
