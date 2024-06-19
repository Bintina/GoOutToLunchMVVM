package com.bintina.goouttolunchmvvm.restaurants.model.database.repository

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.bintina.goouttolunchmvvm.restaurants.model.api.ApiService
import com.bintina.goouttolunchmvvm.utils.MyApp

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

object DataSource {

    suspend fun loadRestaurantList(lifecycleScope: LifecycleCoroutineScope): List<com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant?> {
        val restaurantsDeferred = loadRestaurants(lifecycleScope)

        val restaurantResult = restaurantsDeferred.await()

        return restaurantResult.toMutableList()
    }

    suspend fun loadRestaurants(lifecycleScope: LifecycleCoroutineScope): Deferred<List<com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant?>> =
        lifecycleScope.async(Dispatchers.IO) {
            val apiCall = ApiService.create()

            val response = apiCall.getRestaurants()

            val result = response.results

            MyApp.restaurantList = result
            Log.d("DataSourceRestaurantsLog", "loaded ${result.size}")
            return@async result
        }
}