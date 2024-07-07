package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.work.DownloadWork
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.fetchLocalUserList
import com.bintina.goouttolunchmvvm.user.viewmodel.getLocalUserById
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.bintina.goouttolunchmvvm.utils.objectToJson
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.concurrent.TimeUnit


//Get Places Restaurants and save to Room methods...................................................
fun convertRestaurantToLocalRestaurant(restaurant: Restaurant?): LocalRestaurant? {
    var localRestaurant: LocalRestaurant? = null
    restaurant?.let {

        val photoUrl =
            convertRawUrlToUrl(restaurant)
        /*      Log.d(
                  "RestaurantExtensionsLog",
                  "convertRestaurantToLocalRestaurant photo url is $photoUrl"
              )*/

        val attending = listOf<LocalUser>()
        val attendingString = objectToJson(attending)
        //Log.d("RestaurantExtensionsLog", "attendingString is $attendingString")
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
//        Log.d("RestaurantExtensionsLog", "LocalRestaurant.photoUrl is ${localRestaurant?.photoUrl}. LocalRestaurant is $localRestaurant")

    }
    return localRestaurant
}

fun convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList: List<Restaurant?>): List<LocalRestaurant> {
    if (placesRestaurantList.isNullOrEmpty()) {
        return MyApp.restaurantList as List<LocalRestaurant>
    } else {

        val convertedList: List<LocalRestaurant?> = placesRestaurantList.map { restaurant ->
            convertRestaurantToLocalRestaurant(restaurant)
        }
        return convertedList.filterNotNull()

    }
}


fun saveListToRoomDatabase(result: List<Restaurant>) {

    val placesRestaurantList = result.toMutableList()
    // Convert each Restaurant object to a LocalRestaurant object
    val localRestaurantList =
        convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList)
    //Log.d("RestaurantExtensionsLog", "localRestaurantList is $localRestaurantList")
    //restaurantList = localRestaurantList.toMutableLiveDataList()

    // Get the AppDatabase instance
    val db = MyApp.db

    // Save each LocalRestaurant object to the database
    localRestaurantList.forEach { localRestaurant ->
        Log.d("RestaurantExtensionsLog", "Inserting: $localRestaurant")
        db.restaurantDao().insertRestaurant(localRestaurant!!)
    }
}


suspend fun saveRestaurantListToRoomDatabaseExtension(localRestaurantList: List<LocalRestaurant>) {

    // Get the AppDatabase instance
    val db = MyApp.db

    withContext(Dispatchers.IO) {

        // Save each LocalRestaurant object to the database
        //localRestaurantList.forEach { localRestaurant ->
        Log.d("RestaurantExtensionsLog", "Inserting: $localRestaurantList")
        db.restaurantDao().insertAll(localRestaurantList)
//}
    }
}

//Fetch Room Restaurant methods.....................................................................
fun getClickedRestaurant(restaurant: LocalRestaurant): CurrentUserRestaurant? {
    Log.d("RestaurantExtensionsLog", "getClickedRestaurant restaurant is $restaurant")
    val currentUser = MyApp.currentUser
    val currentClickedUserRestaurant = CurrentUserRestaurant(
        currentUser!!.uid,
        currentUser!!.displayName,
        restaurant.photoUrl,
        restaurant.restaurantId,
        restaurant.name,
        restaurant.latitude,
        restaurant.longitude
    )
    Log.d(
        "RestaurantExtensionsLog",
        "currentClickedUserRestaurant is $currentClickedUserRestaurant"
    )
    return currentClickedUserRestaurant
}
suspend fun getLocalRestaurantById(restaurantId: String): LocalRestaurant {
    // Get the AppDatabase instance
    val db = MyApp.db
    return db.restaurantDao().getRestaurant(restaurantId)
}

fun fetchLocalRestaurantList(): List<LocalRestaurant> {

    var localRestaurants: List<LocalRestaurant?> = listOf()
    CoroutineScope(Dispatchers.IO).launch {
        val db = MyApp.db
        localRestaurants = db.restaurantDao().getAllRestaurants()

        Log.d("RestaurantExtensionLog", "localUsers are $localRestaurants")

    }
    return localRestaurants as List<LocalRestaurant>

}

//Confirm Attending methods.........................................................................
fun confirmAttending(restaurant: CurrentUserRestaurant) {

    CoroutineScope(Dispatchers.IO).launch {

        val localUser = getLocalUserById(restaurant.uid)
        val localRestaurant = getLocalRestaurantById(restaurant.restaurantId)
        Log.d(
            "RestaurantExtensionsLog",
            "confirmAttending called.before updateUserRestaurantChoiceToRoomObject: localUser is $localUser. localRestaurant is $localRestaurant. currentUserRestaurant is $restaurant"
        )

        updateUserResaurantChoiceToRoomObjects(restaurant, localRestaurant, localUser)
        Log.d(
            "RestaurantExtensionsLog",
            "confirmAttending called. after updateUserRestaurantChoiceToRoomObject: localUser is $localUser. localRestaurant is $localRestaurant. currentUserRestaurant is $restaurant"
        )
        uploadToRealtime()
        //download from Realtime
        withContext(Dispatchers.Main) {
            fetchLocalUserList()
        }
    }
}


fun updateUserResaurantChoiceToRoomObjects(
    clickedRestaurant: CurrentUserRestaurant,
    restaurant: LocalRestaurant,
    user: LocalUser
) {
    restaurant.attending += 1
    user.attendingString = restaurant.name

    clickedRestaurant.attending += user

    CoroutineScope(Dispatchers.IO).launch {
        val db = MyApp.db
        db.userDao().insert(user)
        db.restaurantDao().insertRestaurant(restaurant)
    }
    //save userList to Room
    //save restaurantList to Room
}

//Realtime Database methods.........................................................................

fun writeRestaurantsToRealtimeDatabaseExtension(
    localRestaurantList: List<LocalRestaurant>,
    databaseReference: DatabaseReference
) {
    //Writing data to Firebase Realtime Database
    val firebaseUserId = databaseReference.push().key!!

    databaseReference.child("restaurants").child(firebaseUserId).setValue(localRestaurantList)
        .addOnCanceledListener {
            Log.d("RestaurantExtensionLog", "Write to database canceled")
        }
        .addOnFailureListener {
            Log.d("RestaurantExtensionLog", "Write to database failed")
        }

}

fun saveRestaurantsToRealtimeDatabase() {

    val databaseReference = Firebase.database.reference

    val localRestaurantList = fetchLocalRestaurantList()
    Log.d(
        "RestaurantExtensionLog",
        "insertAll has been called. localRestaurantList is $localRestaurantList"
    )
    Log.d("RestaurantExtensionLog", "insertAll has been called")
    writeRestaurantsToRealtimeDatabaseExtension(localRestaurantList, databaseReference)
    Log.d("RestaurantExtensionLog", "writeToRealtimeDatabaseExtension called")
}


fun getRealtimeRestaurants() {
    val databaseReference = Firebase.database.reference

    databaseReference.child("restaurants").get().addOnSuccessListener {
        CoroutineScope(Dispatchers.IO).launch {
            val localRestaurantList = it.getValue() as List<LocalRestaurant>
            saveRestaurantListToRoomDatabaseExtension(localRestaurantList)
        }
    }
}

//Worker methods....................................................................................
fun getWorkManagerStartDelay(): Long {
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

fun setPeriodicWorker(initialDelay: Long, context: Context) {
    val downloadRequest = PeriodicWorkRequestBuilder<DownloadWork>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("key" to "value"))
        .addTag("restaurant")
        .build()

    WorkManager.getInstance(context).enqueue(downloadRequest)
}
