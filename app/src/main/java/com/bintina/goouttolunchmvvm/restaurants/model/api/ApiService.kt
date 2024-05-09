package com.bintina.goouttolunchmvvm.restaurants.model.api

import com.bintina.goouttolunchmvvm.utils.Constants.PLACE_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiService {
    fun create():ApiClient{
        val retrofit = Retrofit.Builder()
            .baseUrl(PLACE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiClient::class.java)
    }
}