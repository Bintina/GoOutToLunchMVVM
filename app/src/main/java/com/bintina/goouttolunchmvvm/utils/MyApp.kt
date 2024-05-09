package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.google.android.libraries.places.api.Places


class MyApp : Application() {

    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context


        var restaurantList = listOf<com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant?>()
        var coworkerList = mutableListOf<User>()

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