package com.bintina.goouttolunchmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add

import androidx.lifecycle.ViewModelProvider
import com.bintina.goouttolunchmvvm.MyApp.Companion.KEY_LOGIN_FRAGMENT
import com.bintina.goouttolunchmvvm.databinding.ActivityMainBinding
import com.bintina.goouttolunchmvvm.login.LogInFragment
import com.bintina.goouttolunchmvvm.login.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment = LogInFragment()


        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(binding.mainFragmentContainerView.id, loginFragment, KEY_LOGIN_FRAGMENT)
        transaction.commit()
    }
}