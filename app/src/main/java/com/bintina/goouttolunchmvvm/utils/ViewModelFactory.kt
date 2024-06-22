package com.bintina.goouttolunchmvvm.user.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao


class ViewModelFactory(
    val application: Application,
    /*private val userDataSource: UserDataRepository,
    private val executor: Executor*/
    //private val userId: Long,
    private val restaurantDao: RestaurantDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                UserViewModel(application, userDao) as T
            }

            modelClass.isAssignableFrom(RestaurantViewModel::class.java) -> {
                RestaurantViewModel(application, restaurantDao) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}