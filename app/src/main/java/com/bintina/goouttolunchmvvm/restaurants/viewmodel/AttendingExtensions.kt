package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.getLocalUserById
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
import com.bintina.goouttolunchmvvm.utils.userListJsonToObject
import com.bintina.goouttolunchmvvm.utils.userListObjectToJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




//Confirm Attending methods.........................................................................
fun confirmAttending(restaurant: LocalRestaurant, currentUser: LocalUser) {
    Log.d(
        "AttendingExtensionsLog",
        "confirmAttending called()."
    )

    //introduce previous restaurant code here?
    CoroutineScope(Dispatchers.IO).launch {
        updateUserRestaurantChoiceToRoomObjects(restaurant, currentUser)
        Log.d(
            "AttendingExtensionsLog",
            "confirmAttending called. after updateUserRestaurantChoiceToRoomObject: localUser is $currentUser. localRestaurant is $restaurant."
        )
        uploadToRealtime()
        //download from Realtime
        /*withContext(Dispatchers.Main) {
            fetchLocalUserList()
        }*/
    }
}


fun updateUserRestaurantChoiceToRoomObjects(
    restaurant: LocalRestaurant,
    user: LocalUser
) {
    Log.d("AttendingExtensionsLog", "updateUserRestaurantChoiceToRoomObjects() called.")
    CoroutineScope(Dispatchers.IO).launch {
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() restaurant.attending is ${restaurant.attendingList} before attending update."
        )
        val usersAttendingObjects = userListJsonToObject(restaurant.attendingList)

        if (!usersAttendingObjects.contains(user)) {
            usersAttendingObjects.add(user)
            Log.d(
                "AttendingExtensionsLog",
                "updateUserRestaurantChoiceToRoomObjects() add $user called."
            )
        } else {
            usersAttendingObjects.remove(user)
            Log.d(
                "AttendingExtensionsLog",
                "updateUserRestaurantChoiceToRoomObjects() remove $user called."
            )
        }
        //Convert back to string
        restaurant.attendingList = userListObjectToJson(usersAttendingObjects)
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() restaurant.attending is ${restaurant.attendingList} after."
        )

        val userAttendingString = user.attendingString ?: ""
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() userAttendingString is ${userAttendingString}."
        )
        if (userAttendingString.isEmpty()) {
            user.attendingString = restaurant.name
        } else {
            Log.d(
                "AttendingExtensionsLog",
                "updateUserRestaurantChoiceToRoomObjects() userAttendingString not empty and else branch triggered."
            )
            cleanUpPreviousSelections(user, userAttendingString)
            user.attendingString = restaurant.name
        }


        val db = MyApp.db
        db.userDao().insert(user)
        db.restaurantDao().insertRestaurant(restaurant)


        //save userList to Room
        //save restaurantList to Room
    }
}


suspend fun cleanUpPreviousSelections(user: LocalUser, userAttendingString: String) {
    Log.d("AttendingExtensionsLog", "cleanUpPreviousSelections() called.")
    //Create CurrentUserRestaurantObject to manipulate user list
    val previousRestaurant = findRestaurantByName(userAttendingString)
    previousRestaurant.let {
        // Convert the attending JSON string to a list of LocalUser
        val attendingList = userListJsonToObject(it.attendingList)
        Log.d("AttendingExtensionsLog", "attendingList is $attendingList")
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
