package com.bintina.goouttolunchmvvm.utils

import android.app.Application
import android.content.Context
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant


class MyApp: Application() {

    companion object{

        // Application instance
        lateinit var instance: MyApp
            private set

        lateinit var myContext: Context


        var restaurantList = mutableListOf<Restaurant>()
        var coworkerList = mutableListOf<User>()
    }

    override fun onCreate(){
        super.onCreate()
        instance = this
        myContext = this
    }
}