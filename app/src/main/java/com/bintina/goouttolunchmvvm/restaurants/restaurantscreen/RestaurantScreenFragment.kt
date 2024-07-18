package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantScreenBinding
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.confirmAttending
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.loadImage


class RestaurantScreenFragment : Fragment() {

    private val TAG = "RestaurantScreenFragLog"
    private var _binding: FragmentRestaurantScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: Adapter
    private lateinit var viewModel: RestaurantViewModel
    private var currentRestaurant: LocalRestaurant = LocalRestaurant()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantScreenBinding.inflate(inflater, container, false)
Log.d(TAG, "onCreateView called.")
        viewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireActivity())).get(
                RestaurantViewModel::class.java
            )
Log.d(TAG, "viewModel instantiated")
        //downloadRealtimeUpdates()
        binding.attendingButton.setOnClickListener {

                confirmAttending(currentRestaurant)
        }

        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        Log.d(TAG, "viewModel.currentRestaurant is ${viewModel.currentRestaurant} and currentRestaurant is $currentRestaurant")
        currentRestaurant = MyApp.currentRestaurant
        Log.d(
            TAG,
            "viewModel.currentClickedRestaurant is ${currentRestaurant}"
        )

        Log.d(TAG, "currentRestaurant is ${currentRestaurant}")
        binding.attendanceRecycler.layoutManager = LinearLayoutManager(requireContext())

        Log.d(TAG, "profile photo url is ${currentRestaurant.photoUrl}")
        loadImage("${currentRestaurant.photoUrl}", binding.restaurantImage)
        binding.restaurantName.text = currentRestaurant.name
        binding.restaurantStyleAndAddress.text = currentRestaurant!!.address

        //Set recyclerView adapter
        adapter = Adapter()
        binding.attendanceRecycler.adapter = adapter


        viewModel.restaurantList.observe(viewLifecycleOwner, { restaurantList ->
            adapter.attendingList = viewModel.getAttendingList(viewModel.currentRestaurant!!)
            Log.d(TAG, "attendingList is ${adapter.attendingList.toString()}")
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}