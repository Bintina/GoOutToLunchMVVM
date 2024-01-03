package com.bintina.goouttolunchmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bintina.goouttolunchmvvm.MyApp.Companion.KEY_LOGIN_FRAGMENT
import com.bintina.goouttolunchmvvm.login.LogInFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivityLog", "Activity Main created")
        val notificationDisplayFragment = LogInFragment()

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(
            R.id.main_fragment_container,
            notificationDisplayFragment,
            KEY_LOGIN_FRAGMENT
        )
        transaction.commit()
        Log.d("MainActivityLog", "Fragment commited")
    }
}