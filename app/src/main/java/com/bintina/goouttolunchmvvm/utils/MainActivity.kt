package com.bintina.goouttolunchmvvm.utils

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    //private lateinit var viewModel: UserViewModel

    companion object{
        //KEYS
        const val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




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

        navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        Log.d("MainActivityLog", "Fragment committed")
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
                // Command to navigate to flow_step_one_dest
                navController.navigate(R.id.coworkers_dest)
                //openNotificationsActivity()
                return true
            }

            R.id.restaurant_list_btn -> {
                //openHelpActivity()
                navController.navigate(R.id.restaurants_dest)

                return true
            }

            R.id.restaurant_map_btn -> {
                navController.navigate(R.id.restaurants_dest)

                //openAboutActivity()
                return true
            }


            else -> return super.onOptionsItemSelected(item)

        }
    }
/*
    private fun configureViewModel() {

        viewModel = Injection.provideUserViewModel(MyApp.myContext)

    }
*/

}
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
