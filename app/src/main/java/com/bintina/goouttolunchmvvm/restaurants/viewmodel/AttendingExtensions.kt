package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.util.Log
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.fetchLocalUserList
import com.bintina.goouttolunchmvvm.user.viewmodel.getLocalUserById
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.uploadToRealtime
import com.bintina.goouttolunchmvvm.utils.userListObjectToJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//Fetch Restaurant attending objects

fun getUsersAttendingRestaurant(restaurant: LocalRestaurant): List<LocalUser> {

    val currentRestaurantObject = getClickedRestaurant(restaurant)

    val attendingList = currentRestaurantObject!!.attending
    Log.d("AttendingExtensionsLog", "attendingList is $attendingList")
    return attendingList

}

//Confirm Attending methods.........................................................................
fun confirmAttending(restaurant: CurrentUserRestaurant) {
    Log.d(
        "AttendingExtensionsLog",
        "confirmAttending called()."
    )

    //introduce previous restaurant code here?
    CoroutineScope(Dispatchers.IO).launch {

        val localUser = getLocalUserById(restaurant.uid)
        Log.d("AttendingExtensionsLog", "confirmAttending localUser is $localUser")
        val localRestaurant = getLocalRestaurantById(restaurant.restaurantId)
        Log.d("AttendingExtensionsLog", "confirmAttending localRestaurant is $localRestaurant")
        //Handle attending list


        updateUserRestaurantChoiceToRoomObjects(restaurant, localRestaurant, localUser)
        Log.d(
            "AttendingExtensionsLog",
            "confirmAttending called. after updateUserRestaurantChoiceToRoomObject: localUser is $localUser. localRestaurant is $localRestaurant. currentUserRestaurant is $restaurant"
        )
        uploadToRealtime()
        //download from Realtime
        /*withContext(Dispatchers.Main) {
            fetchLocalUserList()
        }*/
    }
}


fun updateUserRestaurantChoiceToRoomObjects(
    clickedRestaurant: CurrentUserRestaurant,
    restaurant: LocalRestaurant,
    user: LocalUser
) {
    Log.d("AttendingExtensionsLog", "updateUserRestaurantChoiceToRoomObjects() called.")
    CoroutineScope(Dispatchers.IO).launch {
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() restaurant.attending is ${restaurant.attending} before."
        )
        restaurant.attending = userListObjectToJson(clickedRestaurant.attending)
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() restaurant.attending is ${restaurant.attending} after."
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

        val attendingList = clickedRestaurant.attending
        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObjects() restaurant attending list is $attendingList."
        )
        if (!attendingList.contains(user)) {
            attendingList.add(user)
            Log.d(
                "AttendingExtensionsLog",
                "updateUserRestaurantChoiceToRoomObjects() add $user called."
            )
        } else {
            attendingList.remove(user)
            Log.d(
                "AttendingExtensionsLog",
                "updateUserRestaurantChoiceToRoomObjects() remove $user called."
            )
        }

        Log.d(
            "AttendingExtensionsLog",
            "updateUserRestaurantChoiceToRoomObject called: localUser is $user. localRestaurant is $clickedRestaurant. currentUserRestaurant is $restaurant"
        )

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
        val attendingList = jsonToUserList(it.attending)
        Log.d("AttendingExtensionsLog", "attendingList is $attendingList")
        // Remove the user from the list
        attendingList.removeIf { attendingUser -> attendingUser.uid == user.uid }

        // Convert the list back to a JSON string
        it.attending = userListObjectToJson(attendingList)

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

fun removeAttending(
    user: LocalUser,
    previousCurrentUserRestaurant: CurrentUserRestaurant
): LocalRestaurant {
    val attendingList = previousCurrentUserRestaurant!!.attending
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

fun jsonToUserList(jsonString: String): MutableList<LocalUser> {
    val type = object : TypeToken<MutableList<LocalUser>>() {}.type
    return Gson().fromJson(jsonString, type)
}