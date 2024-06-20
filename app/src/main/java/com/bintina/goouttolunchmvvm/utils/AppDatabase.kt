package com.bintina.goouttolunchmvvm.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bintina.goouttolunchmvvm.restaurants.model.RealtimeRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

@Database(entities = [User::class,RealtimeRestaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
}