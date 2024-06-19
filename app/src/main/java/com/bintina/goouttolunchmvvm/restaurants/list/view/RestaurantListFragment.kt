package com.bintina.goouttolunchmvvm.restaurants.list.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentRestaurantListBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantListFragment : Fragment(), OnRestaurantClickedListener {

    lateinit var adapter: Adapter

    private var _binding: FragmentRestaurantListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantListBinding.inflate(inflater, container, false)

        initializeViews()

        Log.d("RestaurantListFragLog", "RestaurantListFragment inflated")

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //coroutine for Restaurant list results
        lifecycleScope.launch(Dispatchers.IO) {
            val result = try {
                DataSource.loadRestaurantList(lifecycleScope)
            } catch (e: Exception) {
                Log.d("RestListFragLog", "Error is $e. Cause is ${e.cause}")
                emptyList<Restaurant?>()
            }

            //Update UI
            withContext(Dispatchers.Main) {
                if (result.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Sorry we don't have your restaurants",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    adapter.restaurantList = result
                    adapter.notifyDataSetChanged()
                    Log.d(
                        "RestListFragLog",
                        "result list has ${result.size} items. Adapter list has ${adapter.restaurantList.size}"
                    )
                    initializeViews()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeViews() {
        // Set LayoutManager
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = Adapter()
        Log.d("RestListFragLog", "adapterlist has ${adapter.restaurantList.size}")
        binding.restaurantRecyclerView.adapter = adapter
        Log.d("RestListFragLog", "initializeViews called.")
        adapter.listener = this
    }

    override fun onRestaurantClick() {
        TODO("Not yet implemented")
    }

}