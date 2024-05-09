package com.bintina.goouttolunchmvvm.restaurants.model.api

import com.bintina.goouttolunchmvvm.restaurants.model.RestaurantResult
import com.bintina.goouttolunchmvvm.utils.Constants.GEOLOCATE_END_URL
import com.bintina.goouttolunchmvvm.utils.Constants.PLACE_END_URL
import com.bintina.goouttolunchmvvm.utils.MyApp
import retrofit2.http.GET

interface ApiClient {
    @GET(PLACE_END_URL)
    suspend fun getRestaurants(): com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.RestaurantResult
}