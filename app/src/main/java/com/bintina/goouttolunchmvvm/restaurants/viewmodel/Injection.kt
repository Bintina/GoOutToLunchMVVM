package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.content.Context
import androidx.room.Room
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.utils.AppDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {


    private fun provideRestaurantDataSource(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
            .fallbackToDestructiveMigration()
            .build()
    }
    private fun provideRestaurantDao(database: AppDatabase): RestaurantDao {
        return database.restaurantDao()
    }

    private fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val database = provideDatabase(context)
        val restaurantDao = provideRestaurantDao(database)
        val userDao = provideUserDao(database)
        return ViewModelFactory(userDao, restaurantDao)
    }

    fun provideRestaurantViewModel(context: Context): RestaurantViewModel {
        val database = provideDatabase(context)
        val restaurantDao = provideRestaurantDao(database)
        val userDao = provideUserDao(database)
        val factory = ViewModelFactory(userDao, restaurantDao)
        return factory.create(RestaurantViewModel::class.java)
    }

    fun provideRestaurantRepository(context: Context): RestaurantDataRepository {
        val database = provideDatabase(context)
        val dao = provideRestaurantDao(database)
        return RestaurantDataRepository(dao)
    }
}