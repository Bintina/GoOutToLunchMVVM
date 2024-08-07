package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.work.CustomWorkerFactory
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers
import com.bintina.goouttolunchmvvm.model.database.repositories.AppDatabase
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import java.time.LocalDateTime

class MyApp : Application(), androidx.work.Configuration.Provider {


    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context

        //Database instance
        lateinit var db: AppDatabase


        //val restaurantAdapter = Adapter()
        val localRestaurant = LocalRestaurant()
        var currentRestaurant: RestaurantWithUsers = RestaurantWithUsers(localRestaurant, emptyList<LocalUser>())
        var currentAttendingList = listOf<LocalUser>()
        var restaurantList: ArrayList<RestaurantWithUsers?> =
            arrayListOf()
        var coworkerList: ArrayList<LocalUser?> =
            arrayListOf()


        //Date variable
        var currentDate: LocalDateTime = java.time.LocalDateTime.now()


        //Current User
        var currentUser: LocalUser? = null
        var currentClickedRestaurant: RestaurantWithUsers? = null

        //Notification Setting Boolean
        var getNotifications: Boolean = true

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
        get() {
            // Initialize the DataSource here
            val dataSource =
                DataSource

            return androidx.work.Configuration.Builder()
                .setWorkerFactory(CustomWorkerFactory(dataSource))
                .build()
        }
}
