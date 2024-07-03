package com.bintina.goouttolunchmvvm.utils

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

@Database(entities = [LocalUser::class,LocalRestaurant::class], version = 5, autoMigrations = [
    AutoMigration (from = 1, to = 2)
])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
}