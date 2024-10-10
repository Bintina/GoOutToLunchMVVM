package com.bintina.goouttolunchmvvm.user.viewmodel.injection

import android.content.Context
import androidx.room.Room
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.model.database.repositories.AppDatabase
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.ViewModelFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    private fun provideRestaurantDao(database: AppDatabase): RestaurantDao {
        return database.restaurantDao()
    }
    fun provideExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }

    // Initialize Places API and create PlacesClient
    private fun providePlacesClient(context: Context): PlacesClient {
        Places.initialize(context, "YOUR_API_KEY") // Replace with your actual API key
        return Places.createClient(context)
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val userDao = provideUserDao(provideDatabase(context))
       val restaurantDao = provideRestaurantDao(provideDatabase(context))
        val application = MyApp()
        val placesClient = providePlacesClient(context)
        return ViewModelFactory(application, restaurantDao, placesClient, userDao)
    }

    fun provideUserViewModel(context: Context): UserViewModel {
        val userDao = provideUserDao(provideDatabase(context))
        val restaurantDao = provideRestaurantDao(provideDatabase(context))
        val application = MyApp()
        val placesClient = providePlacesClient(context)
        val factory = ViewModelFactory(application, restaurantDao, placesClient, userDao)
        return factory.create(UserViewModel::class.java)
    }

    fun provideRestaurantViewModel(context: Context): com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel {
        val database = provideDatabase(context)
        val restaurantDao = provideRestaurantDao(database)
        val userDao = provideUserDao(database)
        val factory = provideViewModelFactory(context)
        return factory.create(RestaurantViewModel::class.java)
    }
}