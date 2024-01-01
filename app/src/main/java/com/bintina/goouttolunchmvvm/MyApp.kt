package com.bintina.goouttolunchmvvm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {

    companion object{

        //KEYS......................................................................................
        val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"

    }
}