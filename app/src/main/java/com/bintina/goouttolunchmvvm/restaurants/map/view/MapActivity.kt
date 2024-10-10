package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ActivityMainBinding
import com.bintina.goouttolunchmvvm.databinding.ActivityMapBinding
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.signOut
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.ktx.widget.PlaceSelectionError
import com.google.android.libraries.places.ktx.widget.PlaceSelectionSuccess
import com.google.android.libraries.places.ktx.widget.placeSelectionEvents
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMapBinding
    private val TAG = "MapActivityLog"
    private lateinit var databaseReference: DatabaseReference
    private lateinit var viewModel: RestaurantViewModel
    private var restaurantList = emptyList<LocalRestaurant?>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myMap: GoogleMap
    private val fallbackLatLng: LatLng = LatLng(-4.3015359, 39.5744260)
    private lateinit var userLocation: LatLng


    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)

        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.google.android.material.R.color.design_default_color_secondary
            )
        )
        appBarConfiguration = AppBarConfiguration(MyApp.navController.graph)

        viewModel = Injection.provideRestaurantViewModel(this)
        viewModel.getLocalRestaurants()
        restaurantList = MyApp.localRestaurantList

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.my_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment: AutocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.my_autocomplete_fragment)
                    as AutocompleteSupportFragment

        Log.d(
            TAG,
            "map and autocomplete fragments instantiated autocompleteFrag is ${autocompleteFragment.toString()}"
        )

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID,Place.Field.NAME))

        // Listen to place selection events
        Log.d(TAG, "autocompleteFragment instantiated")
        lifecycleScope.launch {
            autocompleteFragment.placeSelectionEvents().collect { event ->
                when (event) {
                    is PlaceSelectionSuccess -> Toast.makeText(
                        this@MapActivity,
                        "Got place '${event.place.name}'",
                        Toast.LENGTH_SHORT
                    ).show()
                    is PlaceSelectionError -> Toast.makeText(
                        this@MapActivity,
                        "Failed to get place '${event.status.statusMessage}'",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        //initializeMap() restructuring consideration.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.coworker_btn -> {
                MyApp.navController.navigate(R.id.coworkers_dest)

                return true
            }

            R.id.restaurant_list_btn -> {
                MyApp.navController.navigate(R.id.restaurant_list_dest)

                return true
            }

            R.id.restaurant_map_btn -> {
                MyApp.navController.navigate(R.id.map_activity)
                Log.d(TAG, "navigate to Maps Fragment called.")
                return true
            }

            R.id.sign_out_btn -> {
                signOut(this)
                MyApp.navController.navigate(R.id.login_dest)

                return true
            }

            R.id.settings_btn -> {
                MyApp.navController.navigate(R.id.settings_dest)

                return true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {


        Log.d(TAG, "onMapReady() called")
        myMap = googleMap

        if (isLocationPermissionGranted()
        ) {
            initializeMapWithLocation()
        } else {
            requestLocationPermission()
        }


        initializeAutocomplete()
        initializeViews(myMap)

    }

    private fun initializeMapWithLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myMap.isMyLocationEnabled = true

            // Get the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    userLocation = if (location != null) {
                        LatLng(location.latitude, location.longitude)
                    } else {
                        fallbackLatLng // Use fallback if location is null
                    }
                    Log.d(TAG, "userLocation value is $userLocation")
                    zoomOnMap(userLocation)
                    addUserMarker(userLocation)
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error retrieving location: ${it.message}")
                    zoomOnMap(fallbackLatLng)
                    addUserMarker(fallbackLatLng)
                }
        } else {
            requestLocationPermission()
        }
    }


    private fun addUserMarker(latLng: LatLng) {
        myMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("You")
        )
    }

    private fun initializeAutocomplete() {
        Log.d(TAG, "initializeAutocomplete() called")
                Places.initializeWithNewPlacesApiEnabled(this, MAPS_API_KEY)

        val center = fallbackLatLng


        val placesClient = Places.createClient(this)

        val circle = CircularBounds.newInstance(center, 5000.0)
        val autocompletePlacesRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery("Piz")
            .setCountries("KE")
            .setLocationRestriction(circle)
            .build()

        placesClient.findAutocompletePredictions(autocompletePlacesRequest)
            .addOnSuccessListener { response ->
                val predictions: List<AutocompletePrediction> = response.autocompletePredictions
                for (prediction in predictions) {
                    Log.d(TAG, "Prediction: ${prediction.getFullText(null)}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "some exception happened: ${exception.message}")
            }

    }

    /*private fun getPlaceFields(): Any {
        TODO("Not yet implemented")
    }
*/
    private fun initializeViews(myMap: GoogleMap) {
        Log.d(TAG, "initializeViews() called")
        /* Toast.makeText(requireContext(), "Map Fragment intializeViews() called ", Toast.LENGTH_LONG)
             .show()*/

        myMap.clear()
        Log.d(TAG, "in initializeViews, restaurantList is $restaurantList")
        restaurantList.let {

            it.forEachIndexed { index, restaurant ->
                restaurant?.let { restaurant ->
                    val position = LatLng(restaurant.latitude, restaurant.longitude)
                    val title = restaurant.name
                    val visited = restaurant.visited
                    //Log.d(TAG, "marker restaurant name is $title")
                    myMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    if (visited == true) {
                                        BitmapDescriptorFactory.HUE_GREEN
                                    } else {
                                        BitmapDescriptorFactory.HUE_RED
                                    }
                                )
                            )
                    )
                    //myMap.setOnMarkerClickListener { true }
                }
            }
        }

    }

    private fun zoomOnMap(latLng: LatLng) {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

    }

    private fun requestLocationPermission() {
        // Check if the user should be shown a rationale for the permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Show a rationale dialog or message
            android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.location_permission_rationale_title))
                .setMessage(getString(R.string.location_permission_rationale))
                .setPositiveButton("OK") { _, _ ->
                    // Request permission after explaining the rationale
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            // No rationale needed, just request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, now you can use location features
                    initializeMapWithLocation()
                } else {
                    // Permission denied, handle accordingly (e.g., disable location features)
                    Log.d(TAG, "Location permission denied.")
                }
            }
        }
    }

    /*  private fun initializeMapWithLocation() {
          if (ActivityCompat.checkSelfPermission(
                  this, Manifest.permission.ACCESS_FINE_LOCATION
              ) == PackageManager.PERMISSION_GRANTED
          ) {
              myMap.isMyLocationEnabled = true
          } else {
              requestLocationPermission() // Ask for permission if not granted
          }
      }*/

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up the PlacesClient
        // Note: There's no direct shutdown() method on PlacesClient, but cleaning up other resources is important.
    }
}