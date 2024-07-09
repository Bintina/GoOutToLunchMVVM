package com.bintina.goouttolunchmvvm.user.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

fun saveRealtimeUserListToRoom(users: List<LocalUser>){

    CoroutineScope(Dispatchers.IO).launch {

        // Get the AppDatabase instance
        val db = MyApp.db

        // Save each LocalUser object to the room database

        db.userDao().insertAll(users)
        Log.d("UserExtensionLog", "Inserting($users) called")
    }
}
//Fetch LocalUser from Room methods
suspend fun fetchLocalUserList(): List<LocalUser?>{
    var localUsers = emptyList<LocalUser?>()
    withContext(Dispatchers.IO) {
        val db = MyApp.db
        localUsers = db.userDao().getAllUsers()
        Log.d("UserExtensionLog", "localUsers are $localUsers")
        localUsers
    }
    return if (localUsers != null) {
        localUsers
    } else {
        emptyList()
    }
}

suspend fun getLocalUserById(uid: String): LocalUser {
    // Get the AppDatabase instance
    val db = MyApp.db
    return db.userDao().getUser(uid)
}

//Firebase Realtime Database methods................................................................
suspend fun saveUsersToRealtimeDatabase(
) {
    val databaseReference = Firebase.database.reference

    val db = MyApp.db
    val localUserList = fetchLocalUserList()
    Log.d("UserExtensionLog", "insertAll has been called. localUserList is $localUserList")
    Log.d("UserExtensionLog", "insertAll has been called")
    if (localUserList.isEmpty()){
        Log.d("UserExtensionLog", "localUserList is empty")
    } else {
        val newUserList: List<LocalUser> = localUserList as List<LocalUser>
    writeUsersToRealtimeDatabaseExtension(newUserList, databaseReference)
    }
    Log.d("UserExtensionLog", "writeToRealtimeDatabaseExtension called")

}



    fun writeUsersToRealtimeDatabaseExtension(localUserList: List<LocalUser>, databaseReference: DatabaseReference) {

    //Writing data to Firebase Realtime Database
    val firebaseUserId = databaseReference.push().key!!

    databaseReference.child("users").child(firebaseUserId).setValue(localUserList)
        .addOnCanceledListener {
            Log.d("UserExtensionLog", "Write to database canceled")
        }
        .addOnFailureListener {
            Log.d("UserExtensionLog", "Write to database failed")
        }

}

fun getRealtimeUsers() {
    val databaseReference = Firebase.database.reference
    databaseReference.child("users").get().addOnSuccessListener {
        val value = it.value
        Log.d("UserExtensionLog", "Realtime Value is: $value")
        saveRealtimeUserListToRoom(value as List<LocalUser>)
    }.addOnFailureListener {
        Log.d("UserExtensionLog", "Failed to read Realtime value.")
    }
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
