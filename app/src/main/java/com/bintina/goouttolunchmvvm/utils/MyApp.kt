package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDateTime


class MyApp : Application() {

    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context

//Database instance

lateinit var db: AppDatabase

        //val restaurantAdapter = Adapter()
        var restaurantList: List<com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant?> = emptyList()


        //Date variable
        var currentDate: LocalDateTime = java.time.LocalDateTime.now()




        //Current User
        var currentUser: FirebaseUser? = null


        //Testing vals
        val sleepDuration: Long = 50000

        lateinit var navController: NavController

        fun initializeDatabase() {
            db = Room.databaseBuilder(
                myContext,
                AppDatabase::class.java, "database-name"
            ).build()
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        myContext = applicationContext

        // Initialize the database
        Companion.initializeDatabase()

        FacebookSdk.sdkInitialize(applicationContext)
        // Initialize the Places SDK
        Places.initialize(applicationContext, "MAP_API_KEY")
    }
}
