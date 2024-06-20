package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

class ViewModelFactory(
    val userDao: UserDao,
    val restaurantDao: RestaurantDao
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RestaurantViewModel::class.java)) {
            RestaurantViewModel(userDao, restaurantDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}