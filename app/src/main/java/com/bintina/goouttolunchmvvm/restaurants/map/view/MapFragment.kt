package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantMapBinding
import com.bintina.goouttolunchmvvm.restaurants.map.viewmodel.Injection
import com.bintina.goouttolunchmvvm.restaurants.map.viewmodel.MapViewModel

class MapFragment(): Fragment(){
    private lateinit var viewModel: MapViewModel

    private var _binding: FragmentRestaurantMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)

        viewModel = Injection.provideRestaurantViewModel(requireContext())
        val restaurant = viewModel.getRestaurant(restaurantId = 10)
        Log.d("MapFragLog", "Restaurant name is ${restaurant.toString()}")

        initializeViews()
        Log.d("MapFragLog","Map Frag onCreateView called")
        return binding.root
    }


    private fun initializeViews() {
        Toast.makeText(requireContext(),"Map Fragment views initialized", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}