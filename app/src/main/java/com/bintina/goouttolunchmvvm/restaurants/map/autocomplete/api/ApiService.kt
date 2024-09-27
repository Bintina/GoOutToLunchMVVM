package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete

import com.bintina.goouttolunchmvvm.utils.Constants
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    fun create(): ApiClient{
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.AUTOCOMPLETE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiClient::class.java)
    }


}