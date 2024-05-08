package com.bintina.goouttolunchmvvm.restaurants.map.viewmodel

import android.content.Context
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.SaveRestaurantDatabase
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import com.bintina.goouttolunchmvvm.utils.MyApp
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {


    private fun provideRestaurantDataSource(context: Context): RestaurantDataRepository {
        val database = SaveRestaurantDatabase.getInstance(context)
        return RestaurantDataRepository(database.restaurantDao())
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceRestaurant = provideRestaurantDataSource(context)
        val executor = provideExecutor()
        val restaurantDao = SaveRestaurantDatabase.getInstance(context).restaurantDao()
        val application = MyApp()
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        return ViewModelFactory(userDao, restaurantDao)
    }

    fun provideRestaurantViewModel(context: Context): MapViewModel {
        val dataSourceRestaurant = provideRestaurantDataSource(context)
        val executor = provideExecutor()
        val restaurantDao = SaveRestaurantDatabase.getInstance(context).restaurantDao()
        val application = MyApp() // Assuming MyApp extends Application
        val userDao = SaveUserDatabase.getInstance(context).userDao()
        val factory = ViewModelFactory(userDao, restaurantDao)
        return factory.create(MapViewModel::class.java)
    }
}