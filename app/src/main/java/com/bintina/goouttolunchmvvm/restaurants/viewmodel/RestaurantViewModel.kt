package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.userListJsonToObject
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RestaurantViewModel(
    application: Application,
    private val restaurantDao: RestaurantDao
) : AndroidViewModel(application) {


    private val TAG = "RestaurantVMLog"
    private var placesRestaurantList = mutableListOf<Restaurant?>()
    var currentRestaurant: LocalRestaurant? = LocalRestaurant()
    var localRestaurantList: List<LocalRestaurant?> = listOf()
    var restaurantList: MutableLiveData<List<LocalRestaurant?>> = MutableLiveData()
    val adapter: Adapter = Adapter()
    var currentRestaurantAttendingList: List<LocalUser?> = listOf()




    private val restaurantDataSource: RestaurantDataRepository =
        RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference

/*
fun selectedRestaurantAttendees(restaurant: LocalRestaurant): List<LocalUser?>{
    Log.d(TAG, "selectRestaurant called.Restaurant selected is $restaurant")
        currentRestaurantAttendingList = getClickedRestaurantAttendeeObjects(restaurant)
    Log.d(TAG, "selectRestaurant called. currentClickedRestaurant is $currentClickedRestaurant")
    return currentRestaurantAttendingList
    }*/



    fun getLocalRestaurants(): MutableLiveData<List<LocalRestaurant?>> {

        viewModelScope.launch(Dispatchers.IO) {
            val result = fetchLocalRestaurantList()

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

    //TODO this method is likely redundant. Replace with method below.
    fun getAttendingList(restaurant: LocalRestaurant): List<LocalUser?>{

        CoroutineScope(Dispatchers.IO).launch {
        val currentLocalRestaurant = getLocalRestaurantById(restaurant.restaurantId)
            val attendingJson = currentLocalRestaurant.attendingList
        withContext(Dispatchers.Main){

            currentRestaurantAttendingList = userListJsonToObject(attendingJson)
        }
        }
        return currentRestaurantAttendingList

    }
    //Fetch Restaurant attending objects
    fun getUsersAttendingRestaurant(restaurant: LocalRestaurant): List<LocalUser> {
        val currentRestaurantAttendingList = getClickedRestaurantAttendeeObjects(restaurant)
        Log.d("AttendingExtensionsLog", "attendingList is $currentRestaurantAttendingList")
        return currentRestaurantAttendingList
    }

    fun setCurrentRestaurant(restaurant: LocalRestaurant): LocalRestaurant{
        Log.d(TAG, "setCurrentRestaurant called. restaurant is $restaurant")

            MyApp.currentRestaurant = restaurant
        Log.d(TAG, "setCurrentRestaurant called. currentRestaurant is $currentRestaurant")
        currentRestaurantAttendingList = getClickedRestaurantAttendeeObjects(restaurant)
        Log.d(TAG, "setCurrentRestaurant called. currentRestaurantAttending is $currentRestaurantAttendingList")

        return currentRestaurant!!
    }


    /*    private fun saveRestaurantToDatabase(restaurant: Restaurant?) {
        restaurant?.let {
            val rawImageUrl = "https://maps.googleapis.com/maps/api/place/photo"
            val photoReference = it.photos.first().photo_reference
            val photoWidth = 400
            val localRestaurant = LocalRestaurant(
                restaurantId = it.place_id,
                name = it.name,
                photoUrl = convertRawUrlToUrl(rawImageUrl, photoWidth.toString(), photoReference)
            )
            viewModelScope.launch(Dispatchers.IO) {
                //restaurantDao.insert(realtimeRestaurant)
                writeToDatabase(localRestaurant)
            }

        }
    }*/

    //.......................Presentation.............................................
    /*    fun initRestaurant(restaurantId: String) {
            if (currentRestaurant != null) {
                return
            }
            currentRestaurant = restaurantDataSource.getRestaurant(restaurantId)

        }*/


    /*fun getAttendingUsers(restaurant: LocalRestaurant?): List<LocalUser?>{
        currentRestaurantAttendingList = restaurant!!.attending
        return currentRestaurantAttendingList
    }*/
    //.......................Logic.......................................................
    /*fun getRestaurant(restaurantId: Long): LiveData<LocalRestaurant>? {
        Log.d("TAG", "restaurant id is $restaurantId")
        return currentRestaurant
    }*/
    /*
    fun addRestaurantAttendanceMethods
     */

}
