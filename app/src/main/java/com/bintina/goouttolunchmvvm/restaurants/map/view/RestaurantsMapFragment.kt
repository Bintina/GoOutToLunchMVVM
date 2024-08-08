package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantMapBinding
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantsMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var myMap: GoogleMap

    private var _binding: FragmentRestaurantMapBinding? = null
    private val binding get() = _binding!!

    private var mutableRestaurantList = MutableLiveData<List<RestaurantWithUsers?>>()
    private var restaurantList = emptyList<RestaurantWithUsers?>()
    private var restaurantMarkerPosition1 = LatLng(0.0, 0.0)
    private var restaurantMarkerTitle1 = ""
    private var markerRestaurant1: Marker? =null
 private var restaurantMarkerPosition2 = LatLng(0.0, 0.0)
    private var restaurantMarkerTitle2 = ""
    private var markerRestaurant2: Marker? =null
 private var restaurantMarkerPosition3 = LatLng(0.0, 0.0)
    private var restaurantMarkerTitle3 = ""
    private var markerRestaurant3: Marker? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)

        Log.d("MapFragLog", "Map Frag onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = Injection.provideRestaurantViewModel(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initializeAutocomplet()
    }

    private fun initializeAutocomplet() {
// Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.search_autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
                val latLng = place.latLng
                if (latLng != null) {
                    zoomOnMap(latLng)
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

    }


    private fun initializeViews() {

        Toast.makeText(requireContext(), "Map Fragment views initialized", Toast.LENGTH_LONG).show()
        mutableRestaurantList.observe(viewLifecycleOwner) { restaurantList ->
            restaurantList?.let {
                // Clear existing markers
                myMap.clear()

                it.forEachIndexed { index, restaurantWithUsers ->
                    restaurantWithUsers?.restaurant?.let { restaurant ->
                        val position = LatLng(restaurant.latitude, restaurant.longitude)
                        val title = restaurant.name
                        myMap.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(title)
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

/*        //ChatGPT suggested
        // Set up initial map settings
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = true*/

        val defaultLatLng = LatLng(-4.3015359, 39.5744260)
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.addMarker(
            MarkerOptions()
                .position(defaultLatLng)
                .title("You")
        )
        myMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng))
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12f))

        markerRestaurant1 = myMap.addMarker(
            MarkerOptions()
                .position(restaurantMarkerPosition1)
                .title(restaurantMarkerTitle1)
        )
        markerRestaurant1!!.tag = 0
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)// 12f -> amount of zoom
        myMap.animateCamera(newLatLngZoom)
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            try {
                val db = MyApp.db
                // Fetch the restaurant list from the repository or ViewModel
                val result = db.restaurantDao().getAllRestaurants()
                mutableRestaurantList.value = result.map { RestaurantWithUsers(it) } // Adjust as needed for your data structure
            } catch (e: Exception) {
                Log.d("RestaurantResultTryCatch", "Error is $e")
                mutableRestaurantList.value = emptyList()
            }
        }

    }
}
