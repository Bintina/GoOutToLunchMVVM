package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete

import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.RestaurantResult
import com.bintina.goouttolunchmvvm.utils.Constants.AUTOCOMPLETE_BASE_URL
import com.google.android.libraries.places.api.Places
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {

    @POST("endpoint") // Replace with your actual endpoint path
    suspend fun sendData(
        @Body requestBody: Restaurant // Replace with your actual request data class
    ): Response<RestaurantResult> // Replace with the response data class
}