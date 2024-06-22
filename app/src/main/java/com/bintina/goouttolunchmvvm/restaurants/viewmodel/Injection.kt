/*
package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.content.Context
import androidx.room.Room
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.viewmodel.ViewModelFactory
import com.bintina.goouttolunchmvvm.utils.AppDatabase
import com.bintina.goouttolunchmvvm.utils.MyApp
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

    fun provideViewModelFactory(context: Context): com.bintina.goouttolunchmvvm.user.viewmodel.ViewModelFactory {
        val database = provideDatabase(context)
        val application = MyApp()
        val restaurantDao = provideRestaurantDao(database)
        val userDao = provideUserDao(database)
        return ViewModelFactory(application, restaurantDao, userDao)
    }

    fun provideRestaurantViewModel(context: Context): com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel {
        val database = provideDatabase(context)
        val restaurantDao = provideRestaurantDao(database)
        val userDao = provideUserDao(database)
        val factory = provideViewModelFactory(context)
        return factory.create(RestaurantViewModel::class.java)
    }

    fun provideRestaurantRepository(context: Context): RestaurantDataRepository {
        val database = provideDatabase(context)
        val dao = provideRestaurantDao(database)
        return RestaurantDataRepository(dao)
    }
}*/
