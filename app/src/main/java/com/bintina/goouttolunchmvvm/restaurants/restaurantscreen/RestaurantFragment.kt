package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantMapBinding
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantScreenBinding

class RestaurantFragment: Fragment() {

    private var _binding: FragmentRestaurantScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}