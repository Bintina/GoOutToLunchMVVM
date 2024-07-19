package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.getLocalUserById
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
import com.bintina.goouttolunchmvvm.utils.userListJsonToObject
import com.bintina.goouttolunchmvvm.utils.userListObjectToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




//Confirm Attending methods.........................................................................
fun confirmAttending(restaurant: LocalRestaurant) {
    Log.d(
        "AttendingExtensionsLog",
        "confirmAttending called()."
    )

    //updateUserRestaurantChoice may need to be waited for before uploadToRealtime runs
    CoroutineScope(Dispatchers.IO).launch {
        updateUserRestaurantChoiceToRoomObjects(restaurant)
        Log.d(
            "AttendingExtensionsLog",
            "confirmAttending called. after updateUserRestaurantChoiceToRoomObject: localUser is ${restaurant.currentUserName}. localRestaurant is $restaurant."
        )
        uploadToRealtime()
        //download from Realtime
        /*withContext(Dispatchers.Main) {
            fetchLocalUserList()
        }*/
    }
}


fun updateUserRestaurantChoiceToRoomObjects(
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
        if(currentUser!!.attendingString.isNotEmpty()){
            val previousRestaurant = restaurantDao.getRestaurantByName(currentUser.attendingString)
            val previousAttendingList = userListJsonToObject(previousRestaurant.attendingList)
            previousAttendingList.remove(currentUser)
            previousRestaurant.attendingList = userListObjectToJson(previousAttendingList)
            restaurantDao.updateRestaurant(previousRestaurant)
        }

        //Add user tto new restaurant's attending list
        val newAttendingList = userListJsonToObject(restaurant.attendingList)
        newAttendingList.add(currentUser)
        restaurant.attendingList = userListObjectToJson(newAttendingList)
        restaurantDao.updateRestaurant(restaurant)

        //Update user's attending string
        currentUser.attendingString = restaurant.restaurantId
        userDao.updateUser(currentUser)
    }
}


suspend fun cleanUpPreviousSelections(user: LocalUser, userAttendingString: String) {
    Log.d("AttendingExtensionsLog", "cleanUpPreviousSelections() called.")
    //Create CurrentUserRestaurantObject to manipulate user list
    val previousRestaurant = findRestaurantByName(userAttendingString)
    previousRestaurant.let {
        // Convert the attending JSON string to a list of LocalUser
        val attendingList = userListJsonToObject(it.attendingList)
        Log.d("AttendingExtensionsLog", "cleanUpPreviousSelections() attendingList is $attendingList")
        // Remove the user from the list
        attendingList.removeIf { attendingUser -> attendingUser.uid == user.uid }

        // Convert the list back to a JSON string
        it.attendingList = userListObjectToJson(attendingList)

        // Update the restaurant in the database
        val db = MyApp.db
        withContext(Dispatchers.IO) {
            db.restaurantDao().insertRestaurant(it)
        }
    }
}

suspend fun findRestaurantByName(userAttendingString: String): LocalRestaurant {

    val db = MyApp.db
    return withContext(Dispatchers.IO) {

        db.restaurantDao().getRestaurantByName(userAttendingString)
    }


}
/*
//Likely Redundant method
fun removeAttending(
    user: LocalUser,
    previousRestaurant: LocalRestaurant
): LocalRestaurant {
    val attendingList = previousRestaurant!!.attendingList
    lateinit var previousLocalRestaurant: LocalRestaurant
    CoroutineScope(Dispatchers.IO).launch {

        if (!attendingList.contains(user)) {
            attendingList.add(user)
        } else {
            attendingList.remove(user)
        }

        previousLocalRestaurant = getLocalRestaurantById(previousCurrentUserRestaurant.restaurantId)
    }
    return previousLocalRestaurant
}
*/
