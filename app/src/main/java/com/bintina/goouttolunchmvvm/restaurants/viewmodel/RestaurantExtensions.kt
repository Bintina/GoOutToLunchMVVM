package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.work.DownloadWork
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.bintina.goouttolunchmvvm.utils.objectToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.ZoneOffset
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
        Log.d("RestaurantExtensionsLog", "attendingString is $attendingString")
        // Use the current date and time for updatedAt
        val currentDateTime = LocalDateTime.now()
        val updatedAt = currentDateTime.toEpochSecond(ZoneOffset.UTC)

        // Use the current date and time minus a year for createdAt (example logic)
        val createdAt = currentDateTime.minusYears(1).toEpochSecond(ZoneOffset.UTC)


        localRestaurant = LocalRestaurant(
            restaurantId = it.place_id,
            name = it.name,
            address = it.vicinity,
            latitude = it.geometry.location.lat,
            longitude = it.geometry.location.lng,
            photoUrl = photoUrl,
            attending = 0,
            createdAt = createdAt,
            updatedAt = updatedAt

        )
        Log.d("RestaurantExtensionsLog", "LocalRestaurant.photoUrl is ${localRestaurant?.photoUrl}. LocalRestaurant is $localRestaurant")

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

fun getWorkManagerStartDelay(): Long{
// Calculate the initial delay to midnight
    val currentTime = Calendar.getInstance()
    val midnightTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, 1)
    }
    val initialDelay = midnightTime.timeInMillis - currentTime.timeInMillis

    return initialDelay
}

fun setPeriodicWorker(initialDelay: Long, context: Context){
    val downloadRequest = PeriodicWorkRequestBuilder<DownloadWork>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("key" to "value"))
        .addTag("restaurant")
        .build()

    WorkManager.getInstance(context).enqueue(downloadRequest)
}
