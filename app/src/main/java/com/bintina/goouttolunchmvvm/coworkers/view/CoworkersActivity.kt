package com.bintina.goouttolunchmvvm.coworkers.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ActivityCoworkersBinding
import com.bintina.goouttolunchmvvm.openCoworkerActivity
import com.bintina.goouttolunchmvvm.openRestaurantListActivity
import com.bintina.goouttolunchmvvm.openRestaurantMapActivity
import com.bintina.goouttolunchmvvm.openSearchActivity


class CoworkersActivity: AppCompatActivity() {
    lateinit var binding: ActivityCoworkersBinding
    companion object {
        // Key for the notification fragment
        const val KEY_COWORKER_FRAGMENT = "KEY_COWORKER_FRAGMENT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoworkersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("CoworkerActivityLog", "CoworkerActivity Inflated")

        setSupportActionBar(binding.myToolbar)
        //Customize the toolbar color
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.google.android.material.R.color.design_default_color_secondary
            )
        )

        //Set up click listener for search button
        val searchBtn = findViewById<View>(R.id.menu_search_btn)
        searchBtn.setOnClickListener {
            openSearchActivity()}

        // Set up the CoworkerFragment
        val coworkerFragment = CoworkerListFragment()
        //coworkerFragment.listener = this

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            R.id.coworker_fragment_container,
            coworkerFragment,
            KEY_COWORKER_FRAGMENT
        )
        transaction.commit()
    }
    //Initialize the contents of the Activity's standard options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //Called when menu item is selected.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.coworker_btn -> {
                openCoworkerActivity()
                return true
            }

            R.id.restaurant_list_btn -> {
                openRestaurantListActivity()
                return true
            }

            R.id.restaurant_map_btn -> {
                openRestaurantMapActivity()
                return true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }
}