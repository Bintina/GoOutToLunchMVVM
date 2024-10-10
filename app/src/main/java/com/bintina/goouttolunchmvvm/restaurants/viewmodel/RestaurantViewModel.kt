package com.bintina.goouttolunchmvvm.restaurants.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEvent
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventError
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventFound
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventLoading
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net.awaitFetchPlace
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net.awaitFindAutocompletePredictions

import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.RestaurantDataRepository
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class RestaurantViewModel (
    application: Application,
    private val placesClient: PlacesClient,
    private val restaurantDao: RestaurantDao
) : AndroidViewModel(application) {


    private val TAG = "RestaurantVMLog"
    private var placesRestaurantList = mutableListOf<Restaurant?>()
    private val restaurantPlaceholder = LocalRestaurant()
    var currentRestaurant: LocalRestaurant? =
        LocalRestaurant()
    var localRestaurantList: List<LocalRestaurant> = emptyList()
    var restaurantList: MutableLiveData<List<LocalRestaurant?>> = MutableLiveData()
    val adapter: Adapter = Adapter()
    var currentRestaurantAttendingList: List<LocalUser?> = listOf()

    private val _restaurantsWithUsers = MutableLiveData<List<RestaurantWithUsers>>()
    val restaurantsWithUsers: LiveData<List<RestaurantWithUsers>> get() = _restaurantsWithUsers
    private var restaurantUsers: List<LocalUser?> = listOf()


    private val restaurantDataSource: RestaurantDataRepository =
        RestaurantDataRepository(restaurantDao)
    lateinit var databaseReference: DatabaseReference

private val _events = MutableLiveData<PlacesSearchEvent>()
    val events: LiveData<PlacesSearchEvent> = _events

    private var searchJob: Job? = null


    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()

        _events.value = PlacesSearchEventLoading

        val handler = CoroutineExceptionHandler { _, throwable ->
            _events.value = PlacesSearchEventError(throwable)
        }
        searchJob = viewModelScope.launch(handler) {
            // Add delay so that network call is performed only after there is a 300 ms pause in the
            // search query. This prevents network calls from being invoked if the user is still
            // typing.
            delay(300)

            val bias: LocationBias = RectangularBounds.newInstance(
                LatLng(-4.3515359, 39.5244260), // SW lat, lng (approx. 5km southwest)
                LatLng(-4.2515359, 39.6244260)  // NE lat, lng (approx. 5km northeast)
            )

            val response = placesClient
                .awaitFindAutocompletePredictions {
                    locationBias = bias
                    typesFilter = listOf(PlaceTypes.RESTAURANT)
                    this.query = query
                    countries = listOf("KE")
                }

            _events.value = PlacesSearchEventFound(response.autocompletePredictions)
        }
    }


    fun onAutocompletePredictionClicked(prediction: AutocompletePrediction) {
        val handler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            Log.e("PlacesSearchViewModel", e.message, e)
        }
        viewModelScope.launch(handler) {
            val place = placesClient.awaitFetchPlace(
                prediction.placeId,
                listOf(
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,
                    Place.Field.BUSINESS_STATUS
                )
            )
            Log.d("PlacesSearchViewModel", "Got place $place")
        }
    }

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
        Log.d(
            TAG,
            "handleUserSelection() called with userId: $userId, restaurantId: $restaurantId."
        )
        viewModelScope.launch {
            val restaurant = getLocalRestaurantById(restaurantId)
            updateUserWithRestaurantWithUserChoice(MyApp.currentUser!!.uid, restaurant)
        }
    }

    /*  //TODO("Replace with get LocalRestaurants?")
      fun getLocalRestaurantsWithUsers(): MutableLiveData<List<RestaurantWithUsers?>> {

          viewModelScope.launch(Dispatchers.IO) {
              val result = fetchRestaurantsWithUsersList()

              if (result.isEmpty()) {
                  Log.d(TAG, "RestaurantListFragment result is empty")
              } else {
                  Log.d(TAG, "Restaurant result is $result")
                  withContext(Dispatchers.Main) {
                      restaurantList.postValue(result)
                      MyApp.localRestaurantWithUsersList = result

                      Log.d(
                          TAG,
                          "Restaurant getRestaurants method result has ${result.size} items. " +
                                  "localRestaurantList is ${MyApp.localRestaurantWithUsersList}"
                      )
                  }
              }
          }
          return restaurantList
      }*/

    //TODO("Replace with setCurrentLocalRestaurant")
    fun setCurrentRestaurant(restaurant: LocalRestaurant): LocalRestaurant {
        Log.d(TAG, "setCurrentRestaurant called. restaurant is $restaurant")
        viewModelScope.launch(Dispatchers.Main) {

            MyApp.currentRestaurant = restaurant
            currentRestaurant = restaurant
            Log.d(TAG, "setCurrentRestaurant called. currentRestaurant is $currentRestaurant")
            currentRestaurantAttendingList =
                withContext(Dispatchers.IO) { getRestaurantUsers(restaurant.restaurantId) }
            Log.d(
                TAG,
                "setCurrentRestaurant called. currentRestaurantAttending is $currentRestaurantAttendingList"
            )
        }

        return currentRestaurant!!
    }


    suspend fun getClickedRestaurantAttendeeObjects(restaurantId: String): List<LocalUser?> {
        restaurantUsers = getRestaurantUsers(restaurantId)

        MyApp.currentAttendingList = restaurantUsers


        return MyApp.currentAttendingList
    }

    fun getLocalRestaurants(): List<LocalRestaurant> {
        viewModelScope.launch {

            MyApp.localRestaurantList = fetchLocalRestaurantList()
            Log.d(TAG, "MyApp.localRestaurantList is ${MyApp.localRestaurantList}")
            localRestaurantList = MyApp.localRestaurantList
            Log.d(TAG, "viewModel.localRestaurantList is ${localRestaurantList}")
        }
        return MyApp.localRestaurantList
    }


   /* suspend fun sendPostRequest(requestData: Restaurant): Result? {
        return try {
            val response = apiClient.sendData(requestData)
            if (response.isSuccessful){
                response
            } else {
                null
            }
        } catch (e: Exception){
            Log.d("RestaurantDataRepositoryLog", "sendPostRequest called and failed error: $e.")
            null
        }
    }*/
}
