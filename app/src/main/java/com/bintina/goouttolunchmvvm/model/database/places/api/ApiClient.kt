package com.bintina.goouttolunchmvvm.model.database.places.api

import com.bintina.goouttolunchmvvm.utils.Constants.PLACE_END_URL
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiClient {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET(PLACE_END_URL)
    suspend fun getRestaurants(): com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.RestaurantResult
}