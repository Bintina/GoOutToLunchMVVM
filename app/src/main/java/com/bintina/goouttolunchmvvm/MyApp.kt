package com.bintina.goouttolunchmvvm

import android.app.Application
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant


class MyApp: Application() {

    companion object{
        var restaurantList = mutableListOf<Restaurant>()
        var coworkerList = mutableListOf<User>()
    }
}