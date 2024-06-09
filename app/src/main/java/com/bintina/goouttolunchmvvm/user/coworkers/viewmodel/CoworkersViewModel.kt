package com.bintina.goouttolunchmvvm.user.coworkers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.UserX
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp


class CoworkersViewModel(
   // private val userDataSource: UserDataRepository,
    private val userDao: UserDao
): ViewModel(){

    init {
        Log.d("CoworkerViewModelLog","CoworkerViewModel init block reached")
    }

    //For User
    fun getCoworkers(userId: Long): MutableList<UserX> {
        Log.d("CoWorkerVMLog", "coworker id is $userId")
        return MyApp.coworkerList
    }
}
