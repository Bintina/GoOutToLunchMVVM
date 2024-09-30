package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.os.Bundle
import android.view.Menu
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.ViewAnimator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventError
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventFound
import com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.PlacesSearchEventLoading
import com.bintina.goouttolunchmvvm.restaurants.map.view.adapter.PlacePredictionAdapter
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesSearchActivity : AppCompatActivity() {

    private val viewAnimator: ViewAnimator by lazy {
        findViewById(R.id.view_animator)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    private val adapter = PlacePredictionAdapter()
    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_search)
        setSupportActionBar(findViewById(R.id.toolbar))
        initRecyclerView()
        adapter.onPlaceClickListener = { prediction ->
            viewModel.onAutocompletePredictionClicked(prediction)
        }
        viewModel.events.observe(this) { event ->
            when (event) {
                is PlacesSearchEventLoading -> {
                    progressBar.isIndeterminate = true
                }
                is PlacesSearchEventError -> {
                    progressBar.isIndeterminate = false
                    viewAnimator.displayedChild = 0
                }
                is PlacesSearchEventFound -> {
                    progressBar.isIndeterminate = false
                    adapter.setPredictions(event.places)
                    viewAnimator.displayedChild = if (event.places.isEmpty()) 0 else 1
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.menu_search_btn).actionView as SearchView
        searchView.apply {
            queryHint = getString(R.string.search_a_place)
            isIconifiedByDefault = false
            isFocusable = true
            isIconified = false
            requestFocusFromTouch()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onSearchQueryChanged(newText)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = linearLayoutManager
            adapter = this@PlacesSearchActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@PlacesSearchActivity,
                    linearLayoutManager.orientation
                )
            )
        }
    }

}