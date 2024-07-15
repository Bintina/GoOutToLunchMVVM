package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentClickedRestaurant
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
    var currentRestaurant: LocalRestaurant? = null
    var localRestaurantList: List<LocalRestaurant?> = listOf()
    var restaurantList: MutableLiveData<List<LocalRestaurant?>> = MutableLiveData()
    val adapter: Adapter = Adapter()
    var currentRestaurantAttendingList: List<LocalUser?> = listOf()




    private val restaurantDataSource: RestaurantDataRepository =
        RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference


fun selectRestaurant(restaurant: LocalRestaurant): CurrentUserRestaurant{
    Log.d(TAG, "selectRestaurant called.Restaurant selected is $restaurant")
        MyApp.currentClickedRestaurant = getClickedRestaurantAttendeeObjects(restaurant)
    Log.d(TAG, "selectRestaurant called. currentClickedRestaurant is $currentClickedRestaurant")
    return currentClickedRestaurant!!
    }



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

    fun getAttendingList(restaurant: LocalRestaurant): List<LocalUser?>{

        CoroutineScope(Dispatchers.IO).launch {
        val currentLocalRestaurant = getLocalRestaurantById(restaurant.restaurantId)
            val attendingJson = currentLocalRestaurant.attending
        withContext(Dispatchers.Main){

            currentRestaurantAttendingList = jsonToUserList(attendingJson)
        }
        }
        return currentRestaurantAttendingList

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
