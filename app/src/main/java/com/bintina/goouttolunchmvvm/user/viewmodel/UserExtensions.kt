package com.bintina.goouttolunchmvvm.user.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Scope




fun mapFirebaseUserToLocalUser(firebaseUser: FirebaseUser): LocalUser {
    val downloadDate = System.currentTimeMillis().toLong()
    var refreshedDate = System.currentTimeMillis().toLong()
    return LocalUser(
        displayName = firebaseUser.displayName.toString(),
        uid = firebaseUser.uid.toString(),
        email = firebaseUser.email.toString(),
        profilePictureUrl = firebaseUser.photoUrl.toString(),
        createdAt = downloadDate,
        updatedAt = refreshedDate
    )
}

fun convertFirebaseUserListToLocalUserList(firebaseUserList: List<FirebaseUser?>): List<LocalUser?> {

    val convertedList = firebaseUserList.map { user ->
        mapFirebaseUserToLocalUser(firebaseUser = user!!)
    }
    Log.d(
        "UserExtensionLog",
        "converted list has ${convertedList.size} items. List is $convertedList."
    )
    return convertedList
}

fun saveLocalUserToRoomDatabase(result: FirebaseUser?) {

    // Convert each User object to a LocalRestaurant object
    val localUser =
        mapFirebaseUserToLocalUser(result!!)
    Log.d("UserExtensionLog", "localUserList is $localUser")

    CoroutineScope(Dispatchers.IO).launch {

        // Get the AppDatabase instance
        val db = MyApp.db

        // Save each LocalUser object to the database

        db.userDao().insert(localUser!!)
        Log.d("UserExtensionLog", "Inserting($localUser) called")
    }
}

private fun saveUserToRealtimeDatabase(
    localUserList: List<LocalUser>,
    userDao: UserDao
) {

    val databaseReference = Firebase.database.reference

    userDao.insertAll(localUserList)
    Log.d("UserExtensionLog", "insertAll has been called")
    writeToRealtimeDatabaseExtension(localUserList, databaseReference)
    Log.d("UserExtensionLog", "writeToRealtimeDatabaseExtension called")

}


fun writeToRealtimeDatabaseExtension(localUserList: List<LocalUser>, databaseReference: DatabaseReference) {

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
fun getUpdatedLocalUserList (){

}
