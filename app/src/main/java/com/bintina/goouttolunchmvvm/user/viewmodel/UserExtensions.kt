package com.bintina.goouttolunchmvvm.user.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Array

//Get Firebase Auth User methods....................................................................

// Convert a FirebaseUser object to a LocalUser object
fun mapFirebaseUserToLocalUser(firebaseUser: FirebaseUser): LocalUser {
    val downloadDate = System.currentTimeMillis().toLong()
    var refreshedDate = System.currentTimeMillis().toLong()
    return LocalUser(
        displayName = firebaseUser.displayName.toString(),
        uid = firebaseUser.uid.toString(),
        email = firebaseUser.email.toString(),
        profilePictureUrl = firebaseUser.photoUrl.toString(),
        attendingString = null,
        createdAt = downloadDate,
        updatedAt = refreshedDate
    )
}


//Room Database methods.............................................................................
// Save a LocalUser object to the Room database
fun saveLocalUserToRoomDatabase(result: FirebaseUser?) {

    // Convert each User object to a LocalRestaurant object
    val localUser =
        mapFirebaseUserToLocalUser(result!!)
    Log.d("UserExtensionLog", "localUser is $localUser")

    MyApp.currentUser = localUser
    CoroutineScope(Dispatchers.IO).launch {

        // Get the AppDatabase instance
        val db = MyApp.db

        // Save each LocalUser object to the room database
        db.userDao().insert(localUser)
        Log.d("UserExtensionLog", "Inserting($localUser) called")
    }

}

fun saveRealtimeUserListToRoom(users: List<LocalUser>) {

        Log.d("UserExtensionLog", "saveRealtimeUserListToRoom() called.")
    CoroutineScope(Dispatchers.IO).launch {

        // Get the AppDatabase instance
        val db = MyApp.db

        // Save each LocalUser object to the room database

        db.userDao().insertAll(users)
        Log.d("UserExtensionLog", "Inserting($users) called")
    }
}

//Fetch LocalUser from Room methods
suspend fun fetchLocalUserList(): List<LocalUser> {
        Log.d("UserExtensionLog", "fetchLocalUserList() has been called")
    var localUsers = emptyList<LocalUser>()
    withContext(Dispatchers.IO) {
        val db = MyApp.db
        localUsers = db.userDao().getAllUsers()
        Log.d("UserExtensionLog", "localUsers are $localUsers")
        localUsers
    }
    return localUsers!!
}


suspend fun getLocalUserById(uid: String): LocalUser {
    // Get the AppDatabase instance
    val db = MyApp.db
    return db.userDao().getUser(uid)
}

//Firebase Realtime Database methods................................................................
suspend fun saveUsersToRealtimeDatabase() {
    val databaseReference = Firebase.database.reference

    CoroutineScope(Dispatchers.Main).launch {

        val localUserList: List<LocalUser> = withContext(Dispatchers.IO) { fetchLocalUserList() }
        Log.d("UserExtensionLog", "insertAll has been called. localUserList is $localUserList")


        writeUsersToRealtimeDatabaseExtension(localUserList, databaseReference)
        Log.d("UserExtensionLog", "writeToRealtimeDatabaseExtension called")


    }
}


fun writeUsersToRealtimeDatabaseExtension(
    localUserList: List<LocalUser>,
    databaseReference: DatabaseReference
) {
    Log.d("UserExtensionLog", "writeUsersToRealtimeDatabaseExtension() called.")
    localUserList.forEach { localUser ->
        val userQuery = databaseReference.child("users").orderByChild("uid").equalTo(localUser.uid)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User with the same uid exists, update the existing user's data
                    for (userSnapshot in dataSnapshot.children) {
                        userSnapshot.ref.setValue(localUser)
                            .addOnSuccessListener {
                                Log.d(
                                    "UserExtensionLog",
                                    "User data updated successfully for uid: ${localUser.uid}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.d(
                                    "UserExtensionLog",
                                    "Failed to update user data for uid: ${localUser.uid}, error: ${e.message}"
                                )
                            }
                    }
                } else {
                    // No user with the same uid, create a new user entry
                    val firebaseUserId = databaseReference.child("users").push().key!!
                    databaseReference.child("users").child(firebaseUserId).setValue(localUser)
                        .addOnSuccessListener {
                            Log.d(
                                "UserExtensionLog",
                                "New user data saved successfully for uid: ${localUser.uid}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.d(
                                "UserExtensionLog",
                                "Failed to save new user data for uid: ${localUser.uid}, error: ${e.message}"
                            )
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("UserExtensionLog", "User query cancelled, error: ${databaseError.message}")
            }
        })
    }
}

fun getRealtimeUsers() {
    val databaseReference = Firebase.database.reference
    fetchUsersFromRealtimeDatabase(databaseReference) { users ->
        Log.d("UserExtensionLog", "Fetched users: $users")
        CoroutineScope(Dispatchers.IO).launch {
            saveRealtimeUserListToRoom(users)
        }
    }

}

fun fetchUsersFromRealtimeDatabase(
    databaseReference: DatabaseReference,
    onUsersFetched: (ArrayList<LocalUser>) -> Unit
) {
    Log.d("UserExtensionLog", "fetchUsersFromRealtimeDatabase() called.")
    databaseReference.child("users").addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            //CoroutineScope(Dispatchers.IO).launch {
            val localUserList = arrayListOf<LocalUser>()
            Log.d(
                "UserExtensionLog",
                "onDataChangedCalled. localUserList size is ${localUserList.size}. ${localUserList}"
            )
            for (userSnapshot in dataSnapshot.children) {
                val user = userSnapshot.getValue(LocalUser::class.java)
                Log.d("UserExtensionLog", "userSnapshot.getValue called. user is $user")
                user?.let { localUserList.add(it) }
            }
            //withContext(Dispatchers.Main) {
            onUsersFetched(localUserList)
            //}
            //}
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("UserExtensions", "Failed to fetch users: ${databaseError.message}")
        }
    })
}

fun mapToLocalUser(data: Map<String, Any?>): LocalUser {
    return LocalUser(
        createdAt = data["createdAt"] as? Long ?: 0,
        displayName = data["displayName"] as? String ?: "",
        email = data["email"] as? String ?: "",
        profilePictureUrl = data["profilePictureUrl"] as? String ?: "",
        uid = data["uid"] as? String ?: "",
        updatedAt = data["updatedAt"] as? Long ?: 0
    )
}
//JSON methods......................................................................................

fun userObjectToJson(user: LocalUser): String {
    val attendingJsonString = Gson().toJson(user)

    return attendingJsonString
}

fun <T> userJsonToObject(attendingJsonString: String, clazz: Class<T>): List<T> {
    val typeToken = object : TypeToken<List<LocalUser>>() {}.type
    return Gson().fromJson(attendingJsonString, typeToken)
}
