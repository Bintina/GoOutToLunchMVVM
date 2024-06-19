package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.restaurants.model.RealtimeRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RestaurantViewModel(
    private val userDao: UserDao,
    private val restaurantDao: RestaurantDao
): ViewModel(){


    private val TAG = "RestaurantViewModelLog"
    private var currentRestaurant: LiveData<RealtimeRestaurant>? = null

    private val userDataSource: UserDataRepository = UserDataRepository(userDao)
    private val restaurantDataSource: RestaurantDataRepository = RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference

    /* fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }*/
    fun initRestaurant(restaurantId: Long) {
        if (currentRestaurant != null) {
            return
        }
        currentRestaurant = restaurantDataSource.getRestaurant(restaurantId)

    }

    fun getRestaurant(restaurantId: Long): LiveData<RealtimeRestaurant>? {
        Log.d("RestMapInjectLog", "restaurant id is $restaurantId")
        return currentRestaurant
    }
    private fun writeToDatabase(restaurant: User) {
        databaseReference = Firebase.database.reference
        //Writing data to Firebase Realtime Database
        val firebaseRestaurantId = databaseReference.push().key!!

        databaseReference.child("restaurants").child(firebaseRestaurantId).setValue(restaurant)
            .addOnCanceledListener {
                Log.d(TAG, "Write to database canceled")
            }
            .addOnFailureListener{
                Log.d(TAG, "Write to database failed")
            }

    }
}
