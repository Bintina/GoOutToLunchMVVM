package com.bintina.goouttolunchmvvm.login.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bintina.goouttolunchmvvm.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.login.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.login.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.login.view.MyLogInFragment
import com.bintina.goouttolunchmvvm.login.viewmodel.injection.Injection
import com.facebook.login.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executor


class UserViewModel(
    application: Application,
    private val userDataSource: UserDataRepository,
    private val userDao: UserDao,
    val executor: Executor
) : ViewModel() {

    lateinit var vmExecutor: Executor
    lateinit var vmViewModel: ViewModel

    private var currentUser: LiveData<User>? = null
    private val context = MyApp.myContext
    private val vmViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(context)
    private val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    val vmLogInFragment = MyLogInFragment()
    val KEY_LOGIN_FRAGMENT = "KEY_LOGIN_FRAGMENT"
    val mainContainerInt = R.id.main_fragment_container
    val vmUserDao = userDao


    init {
        vmExecutor = Injection.provideExecutor()
        vmViewModel = UserViewModel(application, UserDataRepository(userDao), userDao, vmExecutor )
    }


    fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataSource.getUser(userId)

    }

    //For User
    fun getUser(userId: Long): LiveData<User>? {
        return currentUser
    }
}