package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.inject

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesClientProvider {

    private val TAG = "PlacesClientProviderLog"
    private var placesClient: PlacesClient? = null

    fun providePlacesClient(context: Context): PlacesClient {
        if (placesClient == null) {
            // Initialize the Places SDK
            if (!Places.isInitialized()) {
                Places.initialize(context.applicationContext, "YOUR_API_KEY")
                Log.d(TAG,"Places not initialized")
            }
            // Create the PlacesClient
            placesClient = Places.createClient(context)
                Log.d(TAG,"Places initialized")
        }
        return placesClient!!
    }
}