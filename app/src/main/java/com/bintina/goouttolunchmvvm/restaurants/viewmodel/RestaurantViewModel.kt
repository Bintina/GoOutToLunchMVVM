package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.model.RealtimeRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

    private fun saveRestaurantToDatabase(restaurant: Restaurant?) {
        restaurant?.let {
            val rawImageUrl = "https://maps.googleapis.com/maps/api/place/photo"
            val photoReference = it.photos.first().photo_reference
            val photoWidth = 400
            val realtimeRestaurant = RealtimeRestaurant(
                restaurantId = it.place_id,
                name = it.name,
                photoUrl = convertRawUrlToUrl(rawImageUrl, photoWidth.toString(), photoReference)
            )
            TODO("create check for users already in database.")
            viewModelScope.launch(Dispatchers.IO) {
                //restaurantDao.insert(realtimeRestaurant)
                writeToDatabase(realtimeRestaurant)
            }
        }
    }
    private fun writeToDatabase(restaurant: RealtimeRestaurant) {
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
