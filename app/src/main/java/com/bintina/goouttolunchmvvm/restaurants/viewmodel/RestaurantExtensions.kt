package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.work.DownloadWork
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
fun convertRestaurantToLocalRestaurant(
    restaurant: Restaurant?,
    currentUser: LocalUser
): LocalRestaurant? {
    var localRestaurant: LocalRestaurant? = null
    restaurant?.let {

        val photoUrl =
            convertRawUrlToUrl(restaurant)
        /*      Log.d(
                  "RestaurantExtensionsLog",
                  "convertRestaurantToLocalRestaurant photo url is $photoUrl"
              )*/


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
            createdAt = createdAt,
            updatedAt = updatedAt,
            visited = false,

        )
//        Log.d("RestaurantExtensionsLog", "LocalRestaurant.photoUrl is ${localRestaurant?.photoUrl}. LocalRestaurant is $localRestaurant")

    }
    return localRestaurant
}

fun convertPlacesRestaurantListToLocalRestaurantList(
    placesRestaurantList: List<Restaurant?>,
    currentUser: LocalUser
): List<LocalRestaurant> {
    if (placesRestaurantList.isNullOrEmpty()) {
        return MyApp.restaurantArrayList as List<LocalRestaurant>
    } else {

        val convertedList: List<LocalRestaurant?> = placesRestaurantList.map { restaurant ->
            convertRestaurantToLocalRestaurant(restaurant, currentUser)
        }
        return convertedList.filterNotNull()

    }
}

/*
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
}*/


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
/*fun getClickedRestaurantAttendeeObjects(restaurant: LocalRestaurant): List<LocalUser> {

    //TODO("convert restaurant.attending json to object here")
    val attendingString = restaurant.attendingList
    MyApp.currentAttendingList = userListJsonToObject(attendingString)


    return MyApp.currentAttendingList
}*/

suspend fun getRestaurantWithUsersById(restaurantId: String): RestaurantWithUsers {
    // TODO("Does this need to handle RestaurantWithUsers?")
    return withContext(Dispatchers.IO){

    val db = MyApp.db
     db.restaurantDao().getRestaurantWithUsers(restaurantId)
    }
}
suspend fun getLocalRestaurantById(restaurantId: String): LocalRestaurant {
    // TODO("Does this need to handle RestaurantWithUsers?")
    return withContext(Dispatchers.IO){

    val db = MyApp.db
     db.restaurantDao().getRestaurant(restaurantId)
    }
}
suspend fun fetchRestaurantsWithUsersList(): List<RestaurantWithUsers> {
    return withContext(Dispatchers.IO) {
        try {
            val db = MyApp.db
            val localRestaurants = db.restaurantDao().getRestaurantsWithUsers()
            Log.d("RestaurantExtensionLog", "localRestaurants are $localRestaurants")
            localRestaurants
        } catch (e: Exception) {
            Log.e("RestaurantExtensionLog", "Error fetching restaurants", e)
            emptyList()
        }
    }
}
suspend fun fetchLocalRestaurantList(): List<LocalRestaurant> {
    return withContext(Dispatchers.IO) {
        try {
            val db = MyApp.db
            val localRestaurants = db.restaurantDao().getAllRestaurants()
            Log.d("RestaurantExtensionLog", "localRestaurants are $localRestaurants")
            localRestaurants
        } catch (e: Exception) {
            Log.e("RestaurantExtensionLog", "Error fetching restaurants", e)
            emptyList()
        }
    }
}

//Realtime Database methods.........................................................................
fun saveRestaurantsToRealtimeDatabase() {

    val databaseReference = Firebase.database.reference

    CoroutineScope(Dispatchers.Main).launch {
        val localRestaurantList = withContext(Dispatchers.IO) { fetchLocalRestaurantList() }
        Log.d(
            "RestaurantExtensionLog",
            "insertAll has been called. localRestaurantList is $localRestaurantList"
        )

        writeRestaurantsToRealtimeDatabaseExtension(localRestaurantList, databaseReference)
        Log.d("RestaurantExtensionLog", "writeToRealtimeDatabaseExtension called")
    }
}

fun writeRestaurantsToRealtimeDatabaseExtension(
    localRestaurantList: List<LocalRestaurant>,
    databaseReference: DatabaseReference
) {
    Log.d("RestaurantExtensionLog", "writeRestaurantsToRealtimeDatabaseExtension() called.")
    localRestaurantList.forEach { localRestaurant ->
        val restaurantQuery = databaseReference.child("restaurants").orderByChild("restaurantId")
            .equalTo(localRestaurant.restaurantId)
        restaurantQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (restaurantSnapshot in snapshot.children) {
                        restaurantSnapshot.ref.setValue(localRestaurant)
                            .addOnSuccessListener {
                                Log.d(
                                    "RestaurantExtensionLog",
                                    "Restaurant data updated successfully for restaurantId ${localRestaurant.name}"
                                )

                            }
                            .addOnFailureListener { e ->
                                Log.d(
                                    "RestaurantExtensionLog",
                                    "Failed to update restaurant data for restaurantId ${localRestaurant.restaurantId}"
                                )

                            }
                    }
                } else {
                    val firebaseRestaurantId = databaseReference.child("restaurants").push().key!!
                    databaseReference.child("restaurants").child(firebaseRestaurantId)
                        .setValue(localRestaurant)
                        .addOnSuccessListener {
                            Log.d(
                                "RestaurantExtensionLog",
                                "New restaurant data saved successfully for restaurantId ${localRestaurant.restaurantId}"
                            )
                        }
                        .addOnFailureListener {
                            Log.d(
                                "RestaurantExtensionLog",
                                "Failed to save restaurant data save for restaurantId ${localRestaurant.restaurantId}"
                            )
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    "RestaurantExtensionLog",
                    "Restaurant query cancelled, error: ${error.message}"
                )
            }
        })
    }

}


fun getRealtimeRestaurants() {
    val databaseReference = Firebase.database.reference

    fetchRestaurantsFromRealtimeDatabase(databaseReference) { restaurants ->
        Log.d("RestaurantExtensionLog", "Fetched restaurants: $restaurants")
        CoroutineScope(Dispatchers.IO).launch {

            saveRestaurantListToRoomDatabaseExtension(restaurants)
        }
    }
}

fun fetchRestaurantsFromRealtimeDatabase(
    databaseReference: DatabaseReference,
    onRestaurantsFetched: (List<LocalRestaurant>) -> Unit
) {
    databaseReference.child("restaurants").addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val restaurantList = mutableListOf<LocalRestaurant>()
            for (restaurantSnapshot in snapshot.children) {
                val restaurant = restaurantSnapshot.getValue(LocalRestaurant::class.java)
                restaurant?.let { restaurantList.add(it) }
            }
            onRestaurantsFetched(restaurantList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("RestaurantExtensionLog", "Fetch from database canceled: ${error.message}")
        }
    })
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
