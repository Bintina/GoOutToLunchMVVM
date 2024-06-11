package com.bintina.goouttolunchmvvm.utils

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ActivityMainBinding
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.navController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


open class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var binding: ActivityMainBinding

    //private lateinit var viewModel: UserViewModel

    companion object{
        //KEYS
        const val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if Google Play Services are available
        if (isGooglePlayServicesAvailable()) {
            Log.d("MainActLog","Google Play Services are available")
        } else {
            // Handle the case where Google Play Services are not available
            Log.d("MainActLog","Google Play Services are not available")
            // For example, you can show an error message or prompt the user to update Google Play Services.
        }


        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        //Customize the toolbar color
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.google.android.material.R.color.design_default_color_secondary
            )
        )
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

            MyApp.navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        instantiateTodaysDate()
        Log.d("MainActivityLog", "Fragment committed")
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        return resultCode == ConnectionResult.SUCCESS
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
                navController.navigate(R.id.coworkers_dest)

                return true
            }

            R.id.restaurant_list_btn -> {
                navController.navigate(R.id.restaurant_list_dest)

                return true
            }

            R.id.restaurant_map_btn -> {
                navController.navigate(R.id.restaurant_map_dest)

                return true
            }

            R.id.sign_out_btn -> {
                signOut()
                navController.navigate(R.id.login_dest)

                return true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }
    private fun signOut(){
        com.firebase.ui.auth.AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }

    }

    //override fun facebookClick() {
        //startFacebookSignIn()
    //}




}
/*
    private fun configureViewModel() {

        viewModel = Injection.provideUserViewModel(MyApp.myContext)

    }
*/

//From onCreate
        /*configureViewModel()


        Log.d("MainActivityLog", "Activity Main created")

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            viewModel.mainContainerInt,
            viewModel.vmLogInFragment,
            viewModel.KEY_LOGIN_FRAGMENT
        )
        Log.d("MainActLog", "viewModel value is ${viewModel.vmLogInFragment}")
        transaction.commit()*/
