package com.bintina.goouttolunchmvvm.restaurants.list.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantListBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter

class RestaurantListFragment(): Fragment(){

    lateinit var adapter: Adapter

    private var _binding: FragmentRestaurantListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantListBinding.inflate(inflater, container, false)

        initializeViews()

        Log.d("RestaurantListFragLog", "RestaurantListFragment inflated")

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeViews() {
        adapter = Adapter()
        binding.recyclerView.adapter = adapter
    }

}