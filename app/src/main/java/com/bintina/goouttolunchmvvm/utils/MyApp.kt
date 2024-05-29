package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.google.android.libraries.places.api.Places
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date


class MyApp : Application() {

    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context


        //val restaurantAdapter = Adapter()
        var restaurantList: List<com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant?> = emptyList()
        var coworkerList = mutableListOf<User>()

        //Date variable
        var currentDate: LocalDateTime = java.time.LocalDateTime.now()


        //Current User
        var currentUser: User? = null

        //Testing vals
        val sleepDuration: Long = 50000

        lateinit var navController: NavController

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        myContext = applicationContext

        // Initialize the Places SDK
        Places.initialize(applicationContext, "MAP_API_KEY")
    }
}