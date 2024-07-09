package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantScreenBinding
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.confirmAttending
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.currentUserRestaurant
import com.bintina.goouttolunchmvvm.utils.loadImage


class RestaurantScreenFragment : Fragment() {

    private val TAG = "RestaurantScreenFragLog"
    private var _binding: FragmentRestaurantScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: Adapter
    private lateinit var viewModel: RestaurantViewModel
    lateinit var currentRestaurant: CurrentUserRestaurant

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantScreenBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
                RestaurantViewModel::class.java
            )

        binding.attendingButton.setOnClickListener {

            Log.d(TAG, " currentUserRestaurant is ${MyApp.currentClickedRestaurant}")
            confirmAttending(MyApp.currentClickedRestaurant!!)
            Log.d(TAG, " currentUserRestaurant is ${MyApp.currentClickedRestaurant} after click")
        }

        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        Log.d(
            TAG,
            "viewModel.currenClickedRestaurant is ${MyApp.currentClickedRestaurant}"
        )
        currentRestaurant = MyApp.currentClickedRestaurant!!
        Log.d(TAG, "currentRestaurant is $currentRestaurant")
        binding.attendanceRecycler.layoutManager = LinearLayoutManager(requireContext())

        Log.d(TAG, "profile photo url is ${currentRestaurant.restaurantPictureUrl}")
        loadImage("${currentRestaurant.restaurantPictureUrl}", binding.restaurantImage)
        binding.restaurantName.text = currentRestaurant.restaurantName
        binding.restaurantStyleAndAddress.text = currentRestaurant.address

        //Set recyclerView adapter
        adapter = Adapter()
        binding.attendanceRecycler.adapter = adapter
        viewModel.restaurantList.observe(viewLifecycleOwner, { restaurantList ->
            adapter.attendingList = currentRestaurant.attending
            Log.d(TAG, "attendingList is ${adapter.attendingList}")
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}