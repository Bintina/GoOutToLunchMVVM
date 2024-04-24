package com.bintina.goouttolunchmvvm.user.login.viewmodel

import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.MockUser
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp


class UserViewModelTest{
    //application,
    private val userDao = UserDao
    private val userDataSource = UserDataRepository(userDao)
    private val viewModel = UserViewModel(userDataSource, userDao)
}