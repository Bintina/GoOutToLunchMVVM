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
import com.bintina.goouttolunchmvvm.restaurants.map.viewmodel.Injection
import com.bintina.goouttolunchmvvm.restaurants.map.viewmodel.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment(): Fragment(), OnMapReadyCallback {
    private lateinit var viewModel: MapViewModel

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
        val restaurant = viewModel.getRestaurant(restaurantId = 10)

        // Log the restaurant name
        restaurant?.let {
            Toast.makeText(requireContext(), "Restaurant name is ${restaurant.toString()}", Toast.LENGTH_SHORT).show()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    private fun initializeViews() {
        Toast.makeText(requireContext(),"Map Fragment views initialized", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }

}