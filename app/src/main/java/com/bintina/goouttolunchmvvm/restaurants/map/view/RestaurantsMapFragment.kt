package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantMapBinding

import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)


        initializeViews()
        Log.d("MapFragLog","Map Frag onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = Injection.provideRestaurantViewModel(requireContext())
        /*val restaurant = viewModel.getRestaurant(restaurantId = 10)

        // Log the restaurant name
        restaurant?.let {
            Toast.makeText(requireContext(), "Restaurant name is $restaurant", Toast.LENGTH_SHORT).show()
        }*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
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
        Toast.makeText(requireContext(),"Map Fragment views initialized", Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        val defaultLatLng = LatLng(-4.3015359, 39.5744260)
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.addMarker(
            MarkerOptions()
                .position(defaultLatLng)
                .title("Marker")
        )
        myMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng))
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12f))
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)// 12f -> amount of zoom
        myMap.animateCamera(newLatLngZoom)
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO){
            val result = try {
                DataSource.loadRestaurantList(lifecycleScope)
            } catch (e: Exception){
                Log.d("RestaurantResultTryCatch", "Error is $e")
                emptyList<LocalRestaurant>()
            }
            Log.d("RestMapFragLog","result has ${result.size} items")
        }
    }
}
