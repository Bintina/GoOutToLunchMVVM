package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RestaurantViewModel(
    application: Application,
    private val restaurantDao: RestaurantDao
) : ViewModel() {


    private val TAG = "RestaurantVMLog"
    private var placesRestaurantList = mutableListOf<Restaurant?>()
    private var currentRestaurant: MutableLiveData<LocalRestaurant>? = null
    private lateinit var localRestaurantList: List<LocalRestaurant?>
    var restaurantList: MutableLiveData<List<LocalRestaurant?>> = MutableLiveData()
    val adapter: Adapter = Adapter()


    private val restaurantDataSource: RestaurantDataRepository =
        RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference


    //.......................Call.............................................
    fun getPlacesRestaurantList() {
        //coroutine for Restaurant list results
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                DataSource.loadRestaurantList(viewModelScope)
            } catch (e: Exception) {
                Log.d("RestListFragLog", "Error is $e. Cause is ${e.cause}")
                emptyList<Restaurant?>()
            }

            //Update UI
            if (result.isEmpty()) {
                Log.d(TAG, "result list is empty")
            } else {
                Log.d(
                    "TAG",
                    "result list has ${result.size} items. Adapter list has ${adapter.restaurantList.size}"
                )
                withContext(Dispatchers.Main) {
                    placesRestaurantList = result.toMutableList()
                    saveListToDatabase(result)
                    /*
                                        val convertedList = result.map { restaurant ->
                                            convertRestaurantToLocalRestaurant(restaurant)
                                        }.toMutableList()
                                        restaurantList = MutableLiveData(convertedList)
                                        adapter.restaurantList = convertedList
                                        adapter.notifyDataSetChanged()

                                        saveListToDatabase(result)
                    */
                }
            }
        }
    }

    //.......................Response.............................................

    fun convertRestaurantToLocalRestaurant(restaurant: Restaurant?): LocalRestaurant? {
        var localRestaurant: LocalRestaurant? = null
        restaurant?.let {

            val photoUrl =
                convertRawUrlToUrl(restaurant)
            Log.d(TAG, "photo url is $photoUrl")

            localRestaurant = LocalRestaurant(
                restaurantId = it.place_id,
                name = it.name,
                photoUrl = photoUrl
            )

        }
        return localRestaurant
    }

    fun convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList: List<Restaurant?>): List<LocalRestaurant?> {

        val convertedList = placesRestaurantList.map { restaurant ->
            convertRestaurantToLocalRestaurant(restaurant)
        }
        return convertedList
    }

    //.......................Database.............................................
    /*    private fun writeToDatabase(restaurant: LocalRestaurant) {
            databaseReference = Firebase.database.reference
            //Writing data to Firebase Realtime Database
            val firebaseRestaurantId = databaseReference.push().key!!

            databaseReference.child("restaurants").child(firebaseRestaurantId).setValue(restaurant)
                .addOnCanceledListener {
                    Log.d(TAG, "Write to database canceled")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Write to database failed")
                }

        }*/
    fun saveListToDatabase(result: List<Restaurant?>) {

        placesRestaurantList = result.toMutableList()
        // Convert each Restaurant object to a LocalRestaurant object
        localRestaurantList =
            convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList)

        //restaurantList = localRestaurantList.toMutableLiveDataList()
        viewModelScope.launch(Dispatchers.IO) {
            // Get the AppDatabase instance
            val db = MyApp.db

            // Save each LocalRestaurant object to the database
            localRestaurantList.forEach { localRestaurant ->
                db.restaurantDao().insertRestaurant(localRestaurant!!)
            }
        }
    }


    fun getRestaurants(): MutableLiveData<List<LocalRestaurant?>> {

        viewModelScope.launch(Dispatchers.IO) {
            val result: MutableList<LocalRestaurant?> = try {
                restaurantDao.getAllRestaurants()
            } catch (e: Exception) {
                Log.d(TAG, "Error is $e. Cause is ${e.cause}")
                mutableListOf()
            }
            if (result.isEmpty()) {
                Log.d(TAG, "CoworkerListFragment result is empty")
            } else {
                withContext(Dispatchers.Main) {
                    restaurantList.postValue(result)
                    Log.d(
                        TAG,
                        "CoworkerListFragment result has ${result.size} items"
                    )
                }
            }

        }
        return restaurantList
        /*var list: MutableList<User?> = mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            list = userDao.getAllUsers()
        }
        return list*/
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


    //.......................Logic.......................................................
    fun getRestaurant(restaurantId: Long): LiveData<LocalRestaurant>? {
        Log.d("TAG", "restaurant id is $restaurantId")
        return currentRestaurant
    }
    /*
    fun addRestaurantAttendanceMethods
     */

}
