package com.bintina.goouttolunchmvvm

import android.content.Context
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@RunWith(AndroidJUnit4::class)
class UserViewModelInstrumentedTest {
//    private lateinit var scenario: FragmentScenario<MyLogInFragment>
    private lateinit var context: Context
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
//        scenario = launchFragmentInContainer<MyLogInFragment>()
        context = ApplicationProvider.getApplicationContext<Context>()
        val application = MyApp.instance
        val executors = Executors.newSingleThreadExecutor() // Example executor
        val userDao: UserDao = mock(UserDao::class.java)
        val mockUserDataSource = UserDataRepository(userDao)
        viewModel = UserViewModel(application, mockUserDataSource, userDao, executors)
    }


    @Test
    fun user_view_model_facebook_button_is_initialized() {
        viewModel.vmLogInFragment.viewModel.facebookLoginBtn.isInitialized

    }
}