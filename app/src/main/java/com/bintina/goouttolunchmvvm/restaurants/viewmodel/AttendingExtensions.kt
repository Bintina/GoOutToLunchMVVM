package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import androidx.work.ListenableWorker
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef
import com.bintina.goouttolunchmvvm.model.UserWithRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
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

//UI Logic..........................................................................................
suspend fun updateUserWithRestaurantWithUserChoice(uid: String, restaurantChoice: LocalRestaurant) {
    Log.d("AttendingExtensionLog", "updateUserWithRestaurantWithUserChoice() called")
    val db = MyApp.db
    withContext(Dispatchers.IO) {
        val userDao = db.userDao()
        val previousSelectionCrossRef = fetchLocalUserWithRestaurant(uid)
        val previousSelection = previousSelectionCrossRef.restaurant
        Log.d(
            "AttendingExtensionLog",
            "updateUserWithRestaurantWithUserChoice(), previous selection is $previousSelection"
        )

        if (previousSelection?.restaurantId == restaurantChoice.restaurantId) {
            val crossRef = UserRestaurantCrossRef(uid, previousSelection.restaurantId)
            userDao.deleteRestaurantUserCrossRef(crossRef)
        } else {
            if (previousSelection != null) {
                val previousCrossRef = UserRestaurantCrossRef(uid, previousSelection.restaurantId)
                userDao.deleteRestaurantUserCrossRef(previousCrossRef)
            }
            val crossRef = UserRestaurantCrossRef(uid, restaurantChoice.restaurantId)
            Log.d(
                "AttendingExtensionLog",
                "updateUserWithRestaurantWithUserChoice(), current selection is $crossRef"
            )
            userDao.insertRestaurantUserCrossRef(crossRef)
            markRestaurantAsVisited(restaurantChoice)

            saveUserWithRestaurantToRealtimeDatabase()
            saveRestaurantsToRealtimeDatabase()
        }
    }
}

suspend fun markRestaurantAsVisited(restaurant: LocalRestaurant) {
    Log.d("AttendingExtensionLog", "markRestaurantAsVisited() called")
    withContext(Dispatchers.IO) {
        restaurant.visited = true
        saveRestaurantsToRealtimeDatabase()
        Log.d("AttendingExtensionLog", "markRestaurantAsVisited() restaurant is $restaurant")
    }
}


//Room Methods......................................................................................

suspend fun fetchLocalUserWithRestaurant(uid: String): UserWithRestaurant {
    Log.d("AttendingExtensionLog", "fetchLocalUserWithRestaurant() called")
    val db = MyApp.db
    var usersWithRestaurants = emptyList<UserWithRestaurant>()
    var userWithRestaurant =
        UserWithRestaurant(LocalUser("", "", "", "", 0L, 0L), LocalRestaurant())
    withContext(Dispatchers.IO) {
        val userDao = db.userDao()

        usersWithRestaurants = userDao.getUsersWithRestaurants()

        // Find the UserWithRestaurant with the matching uid
        userWithRestaurant = usersWithRestaurants.find { it.user?.uid == uid }!!
        //userWithRestaurant = userDao.getUserWithRestaurant(uid)
    }
    Log.d(
        "AttendingExtensionLog",
        "fetchLocalUserWithRestaurant() fetched restaurant is $userWithRestaurant"
    )

    return userWithRestaurant
}

suspend fun getRestaurantUsers(restaurantId: String): List<LocalUser?> {
    Log.d("AttendingExtensionLog", "getRestaurantUsers() called.")
    return withContext(Dispatchers.IO) {
        val db = MyApp.db
        val userDao = db.userDao()

        val usersWithRestauarantList = userDao.getUsersWithRestaurants()

        val restaurantUsers = usersWithRestauarantList
            .filter { it.restaurant?.restaurantId == restaurantId }
            .map { it.user }
        Log.d(
            "AttendingExtensionLog",
            "getRestaurantUsers() finished. Number of users: ${restaurantUsers.size}"
        )
        restaurantUsers
    }

}

suspend fun getUsersRestaurant(uid: String): String? {
    Log.d("AttendingExtensionLog", "getUsersRestaurant() called.")

    return withContext(Dispatchers.IO) {
        val userWithRestaurantObject = fetchLocalUserWithRestaurant(uid)
        val restaurantName = userWithRestaurantObject.restaurant?.name
        Log.d("AttendingExtensionLog", "getUsersRestaurant() restaurantName is $restaurantName")
        restaurantName
    }

}

suspend fun saveUsersWithRestaurantsToRoom(usersWithRestaurants: List<UserWithRestaurant>) {
    val db = MyApp.db
    withContext(Dispatchers.IO) {
        usersWithRestaurants.forEach { userWithRestaurant ->

            val uid = userWithRestaurant.user?.uid
            val restaurantId = userWithRestaurant.restaurant?.restaurantId

            if (uid != null && restaurantId != null){
                val crossRef = UserRestaurantCrossRef(uid, restaurantId)
                db.userDao().insertRestaurantUserCrossRef(crossRef)
            }

        }
    }
}

//Realtime Methods..................................................................................
fun saveUserWithRestaurantToRealtimeDatabase() {
    Log.d("AttendingExtensionLog", "saveUserWithRestaurantToRealtimeDatabase() called.")
    val databaseReference = Firebase.database.reference

    CoroutineScope(Dispatchers.IO).launch {

        val uid = MyApp.currentUser!!.uid
        val localUserWithRestaurant = fetchLocalUserWithRestaurant(uid)
        val userWithRestaurantRef = databaseReference.child("usersWithRestaurants").child(uid)

        userWithRestaurantRef.setValue(localUserWithRestaurant)
            .addOnSuccessListener {
                Log.d(
                    "AttendingExtensionLog",
                    "Data saved successfully for UserId $uid name is ${localUserWithRestaurant?.user!!.displayName}. Restaurant is ${localUserWithRestaurant?.restaurant?.name}"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "AttendingExtensionLog",
                    "Failed to save data for userName ${localUserWithRestaurant?.user!!.displayName}",
                    e
                )
            }
    }
}

fun getUsersWithRestaurantsFromRealtime() {
    Log.d("AttendingExtensionLog", "getUsersWithRestaurantsFromRealtime() called.")
    val databaseReference = Firebase.database.reference

    fetchUsersWithRestaurantsFromRealtime(databaseReference) { users ->
        CoroutineScope(Dispatchers.IO).launch {
            saveUsersWithRestaurantsToRoom(users)
        }
    }
}

fun fetchUsersWithRestaurantsFromRealtime(
    databaseReference: DatabaseReference,
    onUsersWithRestaurantsFetched: (ArrayList<UserWithRestaurant>) -> Unit
) {
    Log.d("AttendingExtensionLog", "fetchUsersWithRestaurantsFromRealtime() called.")
    databaseReference.child("usersWithRestaurants").addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val localUserWithRestaurantList = arrayListOf<UserWithRestaurant>()
            Log.d(
                "AttendingExtensionLog",
                "onDataChangedCalled. localUsersWithRestaurants size is ${localUserWithRestaurantList.size}. ${localUserWithRestaurantList}"
            )
            // Variable to track if there is at least one user other than the current user
            var hasOtherUsers = false

            if (snapshot.hasChildren()) {
                for (userWithRestaurantSnapshot in snapshot.children) {
                    val userWithRestaurant =
                        userWithRestaurantSnapshot.getValue(UserWithRestaurant::class.java)
                    Log.d(
                        "AttendingExtensionLog",
                        "userWithRestaurantSnapshot.getValue called. userWithRestaurant is $userWithRestaurant"
                    )

                    if (userWithRestaurant != null) {
                        // Check if user.uid is not equal to currentUser.uid
                        if (userWithRestaurant.user?.uid != MyApp.currentUser?.uid) {
                            localUserWithRestaurantList.add(userWithRestaurant)
                            hasOtherUsers = true
                        } else {
                            Log.d(
                                "AttendingExtensionLog",
                                "Skipped current user data: ${userWithRestaurant.user?.uid}"
                            )
                        }
                    }
                }

                // Only call the callback if there are valid users to update
                if (hasOtherUsers) {
                    Log.d(
                        "AttendingExtensionLog",
                        "Final localUsersWithRestaurants size is ${localUserWithRestaurantList.size}."
                    )
                    onUsersWithRestaurantsFetched(localUserWithRestaurantList)
                } else {
                    Log.d(
                        "AttendingExtensionLog",
                        "No users found other than the current user. Retaining local changes."
                    )
                    // Optionally, you can call a method to fetch and use existing local data if needed
                    // e.g., `fetchLocalUserWithRestaurant()` to maintain local state
                }
            } else {
                Log.d("AttendingExtensionLog", "Snapshot has no children. Retaining local changes.")
                // Optionally, you can call a method to fetch and use existing local data if needed
                // e.g., `fetchLocalUserWithRestaurant()` to maintain local state
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(
                "AttendingExtensions",
                "Failed to fetch userWithRestaurant: ${error.message}"
            )
            // Call the callback with the existing data or handle error accordingly
            // onUsersWithRestaurantsFetched(localUserWithRestaurantList) if you have a way to retain existing data
        }
    })
}

fun deleteAllUsersWithRestaurantsFromRealtime(callback: (ListenableWorker.Result) -> Unit) {
    Log.d("AttendingExtensionLog", "deleteAllUsersWithRestaurantsFromRealtime() called.")
    val databaseReference = Firebase.database.reference

    CoroutineScope(Dispatchers.IO).launch {
        val userWithRestaurantRef = databaseReference.child("usersWithRestaurants")

        userWithRestaurantRef.removeValue()
            .addOnSuccessListener {
                Log.d("AttendingExtensionLog", "All UserRestaurant Data deleted successfully")
                // Call the callback with a success result
                callback(ListenableWorker.Result.success())
            }
            .addOnFailureListener { e ->
                Log.e("AttendingExtensionLog", "Failed to delete all userRestaurant data", e)
                // Call the callback with a failure result
                callback(ListenableWorker.Result.failure())
            }
    }
}



//..................................................................................................
//new implementations above.
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//..................................................................................................

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

suspend fun findRestaurantWithUser(uid: String): String? {
    return withContext(Dispatchers.IO) {
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

//Room methods

suspend fun saveRestaurantsWithUsersToRoomExtension(restaurantWithUsersList: List<RestaurantWithUsers>) {
    val db = MyApp.db
    withContext(Dispatchers.IO) {
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
    return withContext(Dispatchers.IO) {
        try {
            val db = MyApp.db
            val localRestaurantsWithUsers = db.restaurantDao().getRestaurantsWithUsers()
            Log.d(
                "AttendingExtensionLog",
                "localRestaurantsWithUsers are $localRestaurantsWithUsers"
            )
            localRestaurantsWithUsers
        } catch (e: Exception) {
            Log.e("AttendingExtensionLog", "Error fetching restaurantsWithUsers", e)
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
            val restaurantId = restaurantWithUsers.restaurant.restaurantId
            val restaurantRef = databaseReference.child("restaurantsWithUsers").child(restaurantId)

            restaurantRef.setValue(restaurantWithUsers)
                .addOnSuccessListener {
                    Log.d(
                        "AttendingExtensionLog",
                        "Data saved successfully for restaurantId $restaurantId"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "AttendingExtensionLog",
                        "Failed to save data for restaurantName $restaurantId",
                        e
                    )
                }
        }
    }
}

fun getRestaurantsWithUsersFromRealtimeDatabase() {
    val databaseReference = Firebase.database.reference

    fetchRestaurantsWithUsersFromRealtimeDatabase(databaseReference) { restaurants ->
        Log.d("AttendingExtensionsLog", "Fetched RestaurantsWithUsers: ")
        CoroutineScope(Dispatchers.IO).launch {
            saveRestaurantsWithUsersToRoomExtension(restaurants)
        }
    }


}

fun fetchRestaurantsWithUsersFromRealtimeDatabase(
    databaseReference: DatabaseReference,
    onRestaurantsWithUsersFetched: (ArrayList<RestaurantWithUsers>) -> Unit
) {
    Log.d("AttendingExtensionLog", "fetchRestaurantsWithUsersFromRealtimeDatabase() called.")
    databaseReference.child("restaurantsWithUsers").addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val localRestaurantWithUsersList = arrayListOf<RestaurantWithUsers>()
            Log.d(
                "AttendingExtensionLog",
                "onDataChangedCalled. localRestaurantWithUsers size is ${localRestaurantWithUsersList.size}. ${localRestaurantWithUsersList}"
            )
            for (restaurantWithUsersSnapshot in dataSnapshot.children) {
                val restaurantWithUser =
                    restaurantWithUsersSnapshot.getValue(RestaurantWithUsers::class.java)
                Log.d(
                    "AttendingExtensionLog",
                    "restaurantWithUsersSnapshot.getValue called. restaurantWithUsers is $restaurantWithUser"
                )
                restaurantWithUser?.let { localRestaurantWithUsersList.add(it) }
            }
            //withContext(Dispatchers.Main) {
            onRestaurantsWithUsersFetched(localRestaurantWithUsersList)
            //}
            //}
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(
                "AttendingExtensions",
                "Failed to fetch restaurantWithUsers: ${databaseError.message}"
            )
        }
    })
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
