package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantScreenBinding
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.utils.loadImage

class RestaurantScreenFragment: Fragment(), OnAttendingClickedListener {

    private var _binding: FragmentRestaurantScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: Adapter
    private lateinit var viewModel: RestaurantViewModel

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

        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        adapter = Adapter()
        loadImage("${viewModel.currentRestaurant?.photoUrl}", binding.restaurantImage)
        binding.restaurantName.text = viewModel.currentRestaurant?.name
        binding.restaurantStyleAndAddress.text = /*${viewModel.currentRestaurant?.style} -*/" ${viewModel.currentRestaurant?.address}"
        adapter.attendingList = viewModel.currentRestaurantAttendingList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttendingClick(currentUser: LocalUser, restaurant: LocalRestaurant) {
        TODO("Not yet implemented")
    }
}