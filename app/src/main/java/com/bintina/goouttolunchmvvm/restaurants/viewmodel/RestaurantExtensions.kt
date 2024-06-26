package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.bintina.goouttolunchmvvm.utils.objectToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun convertRestaurantToLocalRestaurant(restaurant: Restaurant?): LocalRestaurant? {
    var localRestaurant: LocalRestaurant? = null
    restaurant?.let {

        val photoUrl =
            convertRawUrlToUrl(restaurant)
        Log.d(
            "RestaurantExtensionsLog",
            "convertRestaurantToLocalRestaurant photo url is $photoUrl"
        )

        val attending = listOf<LocalUser>()
        val attendingString = objectToJson(attending)

        localRestaurant = LocalRestaurant(
            restaurantId = it.place_id,
            name = it.name,
            address = it.vicinity,
            latitude = it.geometry.location.lat,
            longitude = it.geometry.location.lng,
            photoUrl = photoUrl,
            attending = 0

        )
        Log.d("RestaurantExtensionsLog", "LocalRestaurant.photoUrl is ${localRestaurant?.photoUrl}")

    }
    return localRestaurant
}

fun convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList: List<Restaurant?>): List<LocalRestaurant?> {

    val convertedList = placesRestaurantList.map { restaurant ->
        convertRestaurantToLocalRestaurant(restaurant)
    }
    Log.d(
        "RestaurantExtensionsLog",
        "converted list has ${convertedList.size} items. List is $convertedList."
    )
    return convertedList
}

fun saveListToRoomDatabase(result: List<Restaurant?>) {

    val placesRestaurantList = result.toMutableList()
    // Convert each Restaurant object to a LocalRestaurant object
    val localRestaurantList =
        convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList)
    Log.d("RestaurantExtensionsLog", "localRestaurantList is $localRestaurantList")
    //restaurantList = localRestaurantList.toMutableLiveDataList()

    // Get the AppDatabase instance
    val db = MyApp.db

    // Save each LocalRestaurant object to the database
    localRestaurantList.forEach { localRestaurant ->
        Log.d("RestaurantExtensionsLog", "Inserting: $localRestaurant")
        db.restaurantDao().insertRestaurant(localRestaurant!!)
    }
}
