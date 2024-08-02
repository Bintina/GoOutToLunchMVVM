package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RestaurantViewModel(
    application: Application,
    private val restaurantDao: RestaurantDao
) : AndroidViewModel(application) {


    private val TAG = "RestaurantVMLog"
    private var placesRestaurantList = mutableListOf<Restaurant?>()
    private val restaurantPlaceholder = LocalRestaurant()
    var currentRestaurant: RestaurantWithUsers? = RestaurantWithUsers(restaurantPlaceholder, emptyList())
    var localRestaurantList: List<LocalRestaurant?> = listOf()
    var restaurantList: MutableLiveData<List<RestaurantWithUsers?>> = MutableLiveData()
    val adapter: Adapter = Adapter()
    var currentRestaurantAttendingList: List<LocalUser?> = listOf()

    private val _restaurantsWithUsers = MutableLiveData<List<RestaurantWithUsers>>()
    val restaurantsWithUsers: LiveData<List<RestaurantWithUsers>> get() = _restaurantsWithUsers
    private var restaurantUsers: List<LocalUser> = listOf()



    private val restaurantDataSource: RestaurantDataRepository =
        RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference

    fun loadRestaurantsWithUsers() {
        viewModelScope.launch {
            _restaurantsWithUsers.value = getAllRestaurantsWithUsers()
        }
    }
    /**
     * Handles the user's selection of a restaurant by updating the user's association
     * with restaurants in the database. If the user is already associated with a different
     * restaurant, they are removed from that restaurant and added to the selected one.
     *
     * @param userId The ID of the user making the selection.
     * @param restaurantId The ID of the selected restaurant.
     */
    fun handleUserSelection(userId: String, restaurantId: String) {
        Log.d(TAG, "handleUserSelection() called with userId: $userId, restaurantId: $restaurantId.")
        viewModelScope.launch {
            val restaurant = getRestaurantWithUsersById(restaurantId)
            newConfirmAttending(restaurant)
        }
    }

    fun getLocalRestaurants(): MutableLiveData<List<RestaurantWithUsers?>> {

        viewModelScope.launch(Dispatchers.IO) {
            val result = fetchRestaurantsWithUsersList()

            if (result.isEmpty()) {
                Log.d(TAG, "RestaurantListFragment result is empty")
            } else {
                Log.d(TAG, "Restaurant result is $result")
                withContext(Dispatchers.Main) {
                    restaurantList.postValue(result)
                    Log.d(
                        TAG,
                        "Restaurant getRestaurants method result has ${result.size} items. " +
                                "Result is $result"
                    )
                }
            }
        }
        return restaurantList
    }
    fun setCurrentRestaurant(restaurant: RestaurantWithUsers): RestaurantWithUsers {
        Log.d(TAG, "setCurrentRestaurant called. restaurant is $restaurant")

        MyApp.currentRestaurant = restaurant
        Log.d(TAG, "setCurrentRestaurant called. currentRestaurant is $currentRestaurant")
       // currentRestaurantAttendingList = getClickedRestaurantAttendeeObjects(restaurant)
        Log.d(
            TAG,
            "setCurrentRestaurant called. currentRestaurantAttending is $currentRestaurantAttendingList"
        )

        return currentRestaurant!!
    }

   suspend fun getClickedRestaurantAttendeeObjects(restaurantId: String): List<LocalUser> {
        val restaurant =  getRestaurantWithUsers(restaurantId)
        restaurantUsers = restaurant.users

        MyApp.currentAttendingList = restaurantUsers


        return MyApp.currentAttendingList
    }
}
