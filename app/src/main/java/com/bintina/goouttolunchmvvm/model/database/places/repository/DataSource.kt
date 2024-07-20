package com.bintina.goouttolunchmvvm.model.database.places.repository

import android.util.Log
import com.bintina.goouttolunchmvvm.model.database.places.api.ApiService
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

object DataSource {

    suspend fun loadRestaurantList(lifecycleScope: CoroutineScope): List<Restaurant> {
        var populatedRestaurantResult = emptyList<Restaurant>()
        try{
        val restaurantsDeferred = loadRestaurants(lifecycleScope)

        val restaurantResult = restaurantsDeferred.await()
            populatedRestaurantResult = restaurantResult.filterNotNull()
        } catch (e: Exception) {
            Log.d("DataSourceRestaurantsLog", "loadRestaurantList failed: ${e.message}")
        }

        return populatedRestaurantResult
    }

    suspend fun loadRestaurants(lifecycleScope: CoroutineScope): Deferred<List<Restaurant?>> =
        lifecycleScope.async(Dispatchers.IO) {
            val apiCall = ApiService.create()

            val response = apiCall.getRestaurants()

            val result = response.results

            //MyApp.restaurantList = result
            Log.d("DataSourceRestaurantsLog", "loaded ${result.size}")
            return@async result
        }
}