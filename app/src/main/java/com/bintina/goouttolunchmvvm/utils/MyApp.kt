package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context

import androidx.navigation.NavController
import androidx.room.Room

import com.bintina.goouttolunchmvvm.restaurants.work.CustomWorkerFactory
import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDateTime
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.user.model.LocalUser

class MyApp : Application(), androidx.work.Configuration.Provider {


    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context

//Database instance
lateinit var db: AppDatabase


        //val restaurantAdapter = Adapter()
        var restaurantList: List<com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant?> = emptyList()


        //Date variable
        var currentDate: LocalDateTime = java.time.LocalDateTime.now()




        //Current User
        var currentUser: LocalUser? = null


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

    override val workManagerConfiguration: androidx.work.Configuration
        get()  {
            // Initialize the DataSource here
            val dataSource =
                com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource

            return androidx.work.Configuration.Builder()
                .setWorkerFactory(CustomWorkerFactory(dataSource))
                .build()
        }
}
