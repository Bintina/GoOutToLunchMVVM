package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.work.DownloadWork
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.bintina.goouttolunchmvvm.utils.objectToJson
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
    var currentRestaurant: LocalRestaurant? = null
    var localRestaurantList: List<LocalRestaurant?> = listOf()
    var restaurantList: MutableLiveData<List<LocalRestaurant?>> = MutableLiveData()
    val adapter: Adapter = Adapter()
    var currentRestaurantAttendingList: List<LocalUser?> = listOf()

    //WorkManager variables
    internal val outPutWorkInfoItems: LiveData<List<WorkInfo>>
    private val workManager: WorkManager = WorkManager.getInstance(application)

    //WorkManager initialization
    init{
        outPutWorkInfoItems = workManager.getWorkInfosByTagLiveData("restaurant")
    }

    //Create WorkRequest to manage downloads
    internal fun downloadRestaurants(){
     val downloadRequest = OneTimeWorkRequestBuilder<DownloadWork>()
         .setInputData(workDataOf("key" to "value"))
         .addTag("restaurant")
         .build()

         workManager.beginWith(downloadRequest).enqueue()

    }



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
                    saveListToRoomDatabase(result)
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


    fun saveListToRoomDatabase(result: List<Restaurant?>) {

        placesRestaurantList = result.toMutableList()
        // Convert each Restaurant object to a LocalRestaurant object
        localRestaurantList =
            convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList)
        Log.d(TAG, "localRestaurantList is $localRestaurantList")
        //restaurantList = localRestaurantList.toMutableLiveDataList()
        viewModelScope.launch(Dispatchers.IO) {
            // Get the AppDatabase instance
            val db = MyApp.db

            // Save each LocalRestaurant object to the database
            localRestaurantList.forEach { localRestaurant ->
                Log.d(TAG, "Inserting: $localRestaurant")
                db.restaurantDao().insertRestaurant(localRestaurant!!)
            }
        }
    }


    fun getLocalRestaurants(): MutableLiveData<List<LocalRestaurant?>> {

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
