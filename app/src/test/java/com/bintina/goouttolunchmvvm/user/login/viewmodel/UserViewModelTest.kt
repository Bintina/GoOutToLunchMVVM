package com.bintina.goouttolunchmvvm.user.login.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.bintina.goouttolunchmvvm.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import org.mockito.Mockito.mock


class UserViewModelTest{
    val context = ApplicationProvider.getApplicationContext<Context>()
    private val userDao: UserDao = mock(UserDao::class.java)
    private val mockUserDataSource = UserDataRepository(userDao)
   // private val viewModel = UserViewModel(context, mockUserDataSource, userDao)

   /* @Test
    fun user_view_model_facebook_button_is_initialized(){
        viewModel.vmLogInFragment.viewModel.facebookLoginBtn.isInitialized

    }*/
}