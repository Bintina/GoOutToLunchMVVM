package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
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

suspend fun newUpdateUserRestaurantChoiceToRoomObjects(userId: String, newRestaurant: RestaurantWithUsers) {
    val db = MyApp.db
    withContext(Dispatchers.IO) {
        val restaurantDao = db.restaurantDao()
        val currentRestaurantsWithUsers = restaurantDao.getRestaurantsWithUsers()

        // Find and remove user from any previously attended restaurants
        currentRestaurantsWithUsers.forEach { restaurantWithUsers ->
            val users = restaurantWithUsers.users
            if (users.any { it.uid == userId }) {
                val crossRef = UserRestaurantCrossRef(userId, restaurantWithUsers.restaurant.restaurantId)
                restaurantDao.deleteUserRestaurantCrossRef(crossRef)
            }
        }

        // Add user to the new restaurant
        val crossRef = UserRestaurantCrossRef(userId, newRestaurant.restaurant.restaurantId)
        restaurantDao.insertUserRestaurantCrossRef(crossRef)
        markRestaurantAsVisited(newRestaurant.restaurant)
    }
}
/**
 * Confirms attending a restaurant by updating the local and remote databases.
 *
 * @param restaurant The restaurant the user is attending.
 */

suspend fun newConfirmAttending(restaurant: RestaurantWithUsers) {
    Log.d("AttendingExtensionsLog", "confirmAttending called().")

    // Use viewModelScope or another defined scope
    withContext(Dispatchers.IO) {
        newUpdateUserRestaurantChoiceToRoomObjects(MyApp.currentUser!!.uid, restaurant) // Assuming this function updates the user's choice in Room
        Log.d("AttendingExtensionsLog", "After updateUserRestaurantChoiceToRoomObject: localUser is placeholder. localRestaurant is $restaurant.")
        uploadToRealtime() // Assuming this function uploads data to Realtime Database
        // Optionally fetch updated data from Realtime
    }
}
suspend fun confirmAttending(restaurant: RestaurantWithUsers) {
    Log.d(
        "AttendingExtensionsLog",
        "confirmAttending called()."
    )

    //updateUserRestaurantChoice may need to be waited for before uploadToRealtime runs
    CoroutineScope(Dispatchers.IO).launch {
        //updateUserRestaurantChoiceToRoomObjects(restaurant)
        Log.d(
            "AttendingExtensionsLog",
            "confirmAttending called. after updateUserRestaurantChoiceToRoomObject: localUser is placeholder. localRestaurant is $restaurant."
        )
        uploadToRealtime()
        //download from Realtime
        /*withContext(Dispatchers.Main) {
            fetchLocalUserList()
        }*/
    }
}

/**
 * Retrieves a list of restaurants along with their associated users.
 *
 * @return List of RestaurantWithUsers objects.
 */
suspend fun getAllRestaurantsWithUsers(): List<RestaurantWithUsers> {
    Log.d("AttendingExtensionsLog", "getRestaurantsWithUsers() called.")

    var userList = listOf<RestaurantWithUsers>()

    CoroutineScope(Dispatchers.IO).launch {

        val db = MyApp.db
        val userDao = db.userDao()
        val restaurantDao = db.restaurantDao()
        userList = restaurantDao.getRestaurantsWithUsers()
        Log.d("AttendingExtensionsLog", "Fetched RestaurantWithUsers: $userList")
    }

    return userList
}

/**
 * Retrieves a specific restaurant along with its associated users by restaurant ID.
 * @param restaurantId The ID of the restaurant.
 * @return RestaurantWithUsers object containing the restaurant and its users.
 */
suspend fun getRestaurantWithUsers(restaurantId: String): RestaurantWithUsers {
    Log.d(
        "AttendingExtensionsLog",
        "getRestaurantWithUsers() called with restaurantId: $restaurantId."
    )

    return withContext(Dispatchers.IO) {
        val db = MyApp.db
        val restaurantDao = db.restaurantDao()
        restaurantDao.getRestaurantWithUsers(restaurantId)
        val result = restaurantDao.getRestaurantWithUsers(restaurantId)
        Log.d("AttendingExtensionsLog", "Fetched RestaurantWithUsers: $result")
        result
    }
}

/**
 * Adds a user to a restaurant's attending list.
 *
 * @param userId The ID of the user.
 * @param restaurantId The ID of the restaurant.
 */
suspend fun addUserToRestaurant(userId: String, restaurantId: String) {
    Log.d(
        "AttendingExtensionsLog",
        "addUserToRestaurant() called with userId: $userId, restaurantId: $restaurantId."
    )

    CoroutineScope(Dispatchers.IO).launch {

        val db = MyApp.db
        val userDao = db.userDao()
        val restaurantDao = db.restaurantDao()

        val crossRef = UserRestaurantCrossRef(uid = userId, restaurantId = restaurantId)
        restaurantDao.insertUserRestaurantCrossRef(crossRef)
    }
}

/**
 * Removes a user from a restaurant's attending list.
 *
 * @param userId The ID of the user.
 * @param restaurantId The ID of the restaurant.
 */
suspend fun removeUserFromRestaurant(userId: String, restaurantId: String) {
    Log.d(
        "AttendingExtensionsLog",
        "removeUserFromRestaurant() called with userId: $userId, restaurantId: $restaurantId."
    )
    CoroutineScope(Dispatchers.IO).launch {

        val db = MyApp.db
        val userDao = db.userDao()
        val restaurantDao = db.restaurantDao()

        val crossRef = UserRestaurantCrossRef(uid = userId, restaurantId = restaurantId)
        restaurantDao.deleteUserRestaurantCrossRef(crossRef)
        Log.d("AttendingExtensionsLog", "User $userId removed from restaurant $restaurantId.")
    }
}
suspend fun findRestaurantWithUser(uid: String): String?{
    return withContext(Dispatchers.IO){
        val db = MyApp.db
        val restaurantDao = db.restaurantDao()
        val restaurantsWithUsers = restaurantDao.getRestaurantsWithUsers()

        for (restaurantWithUsers in restaurantsWithUsers) {
            if (restaurantWithUsers.users.any { it.uid == uid }) {
                return@withContext restaurantWithUsers.restaurant.name
            }
        }
         null // Return null if no restaurant with the specified user is found
    }
}
/**
 * Updates the user's restaurant choice in the local Room database.
 *
 * @param restaurantsWithUsers List of restaurants with associated users.
 * @param restaurant The restaurant the user is attending.
 */
fun updateUserRestaurantChoiceToRoomObjects(
    restaurantsWithUsers: List<RestaurantWithUsers>,
    restaurant: LocalRestaurant
) {
    val currentUser = MyApp.currentUser
    //Add MyApp currentUser in this method to be safe.
    Log.d("AttendingExtensionsLog", "updateUserRestaurantChoiceToRoomObjects() called.")
    CoroutineScope(Dispatchers.IO).launch {

        val db = MyApp.db
        val userDao = db.userDao()
        val restaurantDao = db.restaurantDao()


        //Remove user from previous restaurant's attending list


        //Add user tto new restaurant's attending list

        //Update user's attending string
        Log.d("AttendingExtensionsLog", "Updating user restaurant choice in Room database.")
        saveRestaurantsWithUsersToRoomExtension(restaurantsWithUsers)
    }
}

/**
 * Finds a restaurant by its name.
 *
 * @param userAttendingString The name of the restaurant.
 * @return The LocalRestaurant object matching the name.
 */
suspend fun findRestaurantByName(userAttendingString: String): LocalRestaurant {
    Log.d(
        "AttendingExtensionsLog",
        "findRestaurantByName() called with name: $userAttendingString."
    )

    val db = MyApp.db
    return withContext(Dispatchers.IO) {

        val result = db.restaurantDao().getRestaurantByName(userAttendingString)
        Log.d("AttendingExtensionsLog", "Fetched Restaurant: $result")
        result
    }


}

suspend fun markRestaurantAsVisited(restaurant: LocalRestaurant) {
    withContext(Dispatchers.IO) {
        restaurant.visited = true
        saveRestaurantsToRealtimeDatabase()
    }
}

//Room methods

suspend fun saveRestaurantsWithUsersToRoomExtension( restaurantWithUsersList: List<RestaurantWithUsers>){
    val db = MyApp.db
    withContext(Dispatchers.IO){
        restaurantWithUsersList.forEach { restaurantWithUsers ->
            // Insert the restaurant
            db.restaurantDao().insertRestaurant(restaurantWithUsers.restaurant)

            // Insert the users and their cross references
            restaurantWithUsers.users.forEach { user ->
                // Insert user (if you have a user DAO, handle user insertion there)
                // db.userDao().insertUser(user)

                // Insert cross reference
                val crossRef =
                    UserRestaurantCrossRef(user.uid, restaurantWithUsers.restaurant.restaurantId)
                db.restaurantDao().insertUserRestaurantCrossRef(crossRef)
            }
        }
    }
}
suspend fun fetchLocalRestaurantsWithUsersList(): List<RestaurantWithUsers> {
return withContext(Dispatchers.IO){
    try{
        val db = MyApp.db
        val localRestaurantsWithUsers = db.restaurantDao().getRestaurantsWithUsers()
        Log.d("AttendingExtensionLog", "localRestaurantsWithUsers are $localRestaurantsWithUsers")
        localRestaurantsWithUsers
    } catch (e: Exception){
        Log.e("AttendingExtensionLog","Error fetching restaurantsWithUsers", e)
        emptyList()
    }
}
}


//Realtime Restaurant Methods

fun saveRestaurantsWithUsersToRealtimeDatabase() {
    val databaseReference = Firebase.database.reference

    CoroutineScope(Dispatchers.IO).launch {
        val localRestaurantsWithUsersList = fetchLocalRestaurantsWithUsersList()

        localRestaurantsWithUsersList.forEach { restaurantWithUsers ->
            val restaurantName = restaurantWithUsers.restaurant.name
            val restaurantRef = databaseReference.child("restaurantsWithUsers").child(restaurantName)

            restaurantRef.setValue(restaurantWithUsers)
                .addOnSuccessListener {
                    Log.d("AttendingExtensionLog", "Data saved successfully for restaurantName $restaurantName")
                }
                .addOnFailureListener { e ->
                    Log.e("AttendingExtensionLog", "Failed to save data for restaurantName $restaurantName", e)
                }
        }
    }
}

fun writeRestaurantsWithUsersToRealtimeDatabaseExtension(
    localRestaurantsWithUsersList: List<RestaurantWithUsers>,
    databaseReference: DatabaseReference
) {
    localRestaurantsWithUsersList.forEach { restaurantWithUsers ->
        val restaurantWithUsersQuery =
            databaseReference.child("restaurantsWithUsers").orderByChild("restaurantId")
                .equalTo(restaurantWithUsers.restaurant.restaurantId)
        restaurantWithUsersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (restaurantWithUsersSnapshot in snapshot.children) {
                        restaurantWithUsersSnapshot.ref.setValue(restaurantWithUsers)
                            .addOnSuccessListener {
                                Log.d(
                                    "AttendingExtensionLog",
                                    "RestaurantWithUsers data updated successfully for restaurantId ${restaurantWithUsers.restaurant.restaurantId}"
                                )

                            }
                            .addOnFailureListener { e ->
                                Log.d(
                                    "AttendingExtensionLog",
                                    "Failed to update restaurantWithUsers data for restaurantId ${restaurantWithUsers.restaurant.restaurantId}"
                                )

                            }
                    }
                } else {
                    val firebaseRestaurantId =
                        databaseReference.child("restaurantsWithUsers").push().key!!
                    databaseReference.child("restaurantsWithUsers").child(firebaseRestaurantId)
                        .setValue(restaurantWithUsers)
                        .addOnSuccessListener {
                            Log.d(
                                "AttendingExtensionLog",
                                "New restaurantWithUsers data saved successfully for restaurantId ${restaurantWithUsers.restaurant.restaurantId}"
                            )
                        }
                        .addOnFailureListener {
                            Log.d(
                                "AttendingExtensionLog",
                                "Failed to save restaurantWithUsers data save for restaurantId ${restaurantWithUsers.restaurant.restaurantId}"
                            )
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    "AttendingExtensionLog",
                    "RestaurantWithUsers query cancelled, error: ${error.message}"
                )
            }
        })
    }
}
//FCM Implementation methods
