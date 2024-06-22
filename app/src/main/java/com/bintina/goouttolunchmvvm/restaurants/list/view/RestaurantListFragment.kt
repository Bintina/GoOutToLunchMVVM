package com.bintina.goouttolunchmvvm.restaurants.list.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantListBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection

class RestaurantListFragment : Fragment(), OnRestaurantClickedListener {

    lateinit var adapter: Adapter

    private var _binding: FragmentRestaurantListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantListBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
                RestaurantViewModel::class.java
            )
        viewModel.getPlacesRestaurantList()
        viewModel.getRestaurants()
        initializeViews()

        Log.d("RestaurantListFragLog", "RestaurantListFragment inflated")

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeViews() {
      /*  viewModel.restaurantList.observe(viewLifecycleOwner) { restaurantList ->
            adapter.restaurantList = restaurantList
            adapter.notifyDataSetChanged()
        }*/

        // Set LayoutManager
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = Adapter()
        viewModel.restaurantList.observe(viewLifecycleOwner, { restaurantList ->
            adapter.restaurantList = restaurantList
            adapter.notifyDataSetChanged()
        })
        Log.d("RestListFragLog", "adapterlist has ${adapter.restaurantList.size}")
        binding.restaurantRecyclerView.adapter = adapter
        Log.d("RestListFragLog", "initializeViews called.")
        adapter.listener = this
    }

    override fun onRestaurantClick() {
        TODO("Not yet implemented")
    }

}