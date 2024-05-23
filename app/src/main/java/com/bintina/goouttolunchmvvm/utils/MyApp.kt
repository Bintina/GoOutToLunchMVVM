package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.google.android.libraries.places.api.Places
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
        var currentDate: Date = Date(2024,1,1,1,1,1)

        //Testing vals
        val sleepDuration: Long = 50000


    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        myContext = applicationContext

        // Initialize the Places SDK
        Places.initialize(applicationContext, "MAP_API_KEY")
    }
}