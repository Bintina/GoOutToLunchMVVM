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
import com.bintina.goouttolunchmvvm.utils.userListObjectToJson
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
import com.bintina.goouttolunchmvvm.utils.userListJsonToObject
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
fun convertRestaurantToLocalRestaurant(restaurant: Restaurant?): LocalRestaurant? {
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
            attending = "",
            createdAt = createdAt,
            updatedAt = updatedAt,
            visited = false

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
//TODO("Convert attending constructor to object list string - for each.")

    val attending = listOf<LocalUser>()
    val attendingString = userListObjectToJson(attending)
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

    //TODO("convert restaurant.attending json to object here")
    val  attendingString = restaurant.attending
    var attending = userListJsonToObject(attendingString)


    Log.d("RestaurantExtensionsLog", "getClickedRestaurant restaurant is $restaurant")
    val currentUser = MyApp.currentUser
    val currentClickedUserRestaurant = CurrentUserRestaurant(
        currentUser!!.uid,
        currentUser!!.displayName,
        restaurant.address,
        restaurant.photoUrl,
        restaurant.restaurantId,
        restaurant.name,
        restaurant.latitude,
        restaurant.longitude,
        attending
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

suspend fun fetchLocalRestaurantList(): List<LocalRestaurant> {
//TODO("jsoning attending constructor to retain it as object list string")
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

//Confirm Attending methods.........................................................................
fun confirmAttending(restaurant: CurrentUserRestaurant) {

    CoroutineScope(Dispatchers.IO).launch {

        val localUser = getLocalUserById(restaurant.uid)
        val localRestaurant = getLocalRestaurantById(restaurant.restaurantId)
       //Handle attending list


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

    //Consider jsoning attending constructor to retain it as object list string
    restaurant.attending = userListObjectToJson(clickedRestaurant.attending)
    user.attendingString = restaurant.name

    val attendingList = clickedRestaurant.attending
    if(!attendingList.contains(user)){
        attendingList.add(user)
    } else {
        attendingList.remove(user)
    }

    Log.d(
        "RestaurantExtensionsLog",
        "updateUserRestaurantChoiceToRoomObject called: localUser is $user. localRestaurant is $clickedRestaurant. currentUserRestaurant is $restaurant"
    )

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

    CoroutineScope(Dispatchers.Main).launch {
        val localRestaurantList = withContext(Dispatchers.IO) { fetchLocalRestaurantList() }
        Log.d(
            "RestaurantExtensionLog",
            "insertAll has been called. localRestaurantList is $localRestaurantList"
        )
        Log.d("RestaurantExtensionLog", "insertAll has been called")
        writeRestaurantsToRealtimeDatabaseExtension(localRestaurantList, databaseReference)
        Log.d("RestaurantExtensionLog", "writeToRealtimeDatabaseExtension called")
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

fun fetchRestaurantsFromRealtimeDatabase(databaseReference: DatabaseReference, onRestaurantsFetched: (List<LocalRestaurant>) -> Unit) {
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
