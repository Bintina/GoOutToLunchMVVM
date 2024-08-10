package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantMapBinding
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class RestaurantsMapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "RestMapFragLog"
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var myMap: GoogleMap

    private var _binding: FragmentRestaurantMapBinding? = null
    private val binding get() = _binding!!

    private var restaurantList = emptyList<LocalRestaurant?>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)

        Log.d(TAG, "Map Frag onCreateView called")
        viewModel = Injection.provideRestaurantViewModel(requireContext())

        viewModel.getLocalRestaurants()
        restaurantList = MyApp.localRestaurantList
        Log.d(TAG, "Maps restaurantList is $restaurantList")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initializeAutocomplet()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


    private fun initializeViews(myMap: GoogleMap) {
        Log.d(TAG, "initializeViews() called")
        Toast.makeText(requireContext(), "Map Fragment intializeViews() called ", Toast.LENGTH_LONG)
            .show()

myMap.clear()
Log.d(TAG, "in initializeViews, restaurantList is $restaurantList")
        restaurantList.let {

            it.forEachIndexed { index, restaurant ->
                restaurant?.let { restaurant ->
                    val position = LatLng(restaurant.latitude, restaurant.longitude)
                    val title = restaurant.name
                    val visited = restaurant.visited
                    Log.d(TAG, "marker restaurant name is $title")
                    myMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(if (visited == true){
                                BitmapDescriptorFactory.HUE_GREEN
                            } else {
                                BitmapDescriptorFactory.HUE_RED
                            })
                    )
                    )
                    //myMap.setOnMarkerClickListener { true }
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {


        Log.d(TAG, "onMapReady() called")
        myMap = googleMap




        initializeViews(myMap)
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
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 16f))


    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)// 12f -> amount of zoom
        myMap.animateCamera(newLatLngZoom)
    }

    override fun onResume() {
        super.onResume()


    }
}
