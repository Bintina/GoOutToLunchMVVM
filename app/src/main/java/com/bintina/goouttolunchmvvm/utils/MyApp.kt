package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.UserWithRestaurant
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.bintina.goouttolunchmvvm.model.database.repositories.AppDatabase

import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import java.time.LocalDateTime

class MyApp : Application()//, androidx.work.Configuration.Provider
 {


    companion object {

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context

        //Database instance
        lateinit var db: AppDatabase


        //val restaurantAdapter = Adapter()
        val localRestaurant = LocalRestaurant()
        var currentRestaurant: LocalRestaurant = LocalRestaurant()
        var currentAttendingList = listOf<LocalUser?>()
        var restaurantArrayList: ArrayList<LocalRestaurant?> =
            arrayListOf()
        var localUserWithRestaurantList : List<UserWithRestaurant> = emptyList()
        var localRestaurantList : List<LocalRestaurant> = emptyList()
        var currentUserWithRestaurant: UserWithRestaurant = com.bintina.goouttolunchmvvm.model.UserWithRestaurant(
            null, null)
        var coworkerList: ArrayList<LocalUser?> =
            arrayListOf()


        //Date variable
        var currentDate: LocalDateTime = java.time.LocalDateTime.now()


        //Current User
        var currentUser: LocalUser? = null


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

   /* override val workManagerConfiguration: androidx.work.Configuration
        get() {
            // Initialize the DataSource here
            val dataSource =
                DataSource

            return androidx.work.Configuration.Builder()
                .setWorkerFactory(CustomWorkerFactory(dataSource))
                .build()
        }*/
}
