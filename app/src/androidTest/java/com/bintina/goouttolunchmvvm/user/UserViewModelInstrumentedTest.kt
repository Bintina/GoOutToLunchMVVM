package com.bintina.goouttolunchmvvm.user

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import com.bintina.goouttolunchmvvm.user.login.viewmodel.LoginViewModel
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.model.database.repositories.UserDataRepository
import com.bintina.goouttolunchmvvm.utils.MyApp
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.util.concurrent.Executors
import com.bintina.goouttolunchmvvm.user.login.view.MyLogInFragment
import org.junit.runners.JUnit4

//TODO 2. This test needs to change to match VM
@RunWith(JUnit4::class)
class UserViewModelInstrumentedTest {
    private lateinit var scenario: FragmentScenario<MyLogInFragment>
    private lateinit var context: Context
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer<MyLogInFragment>()
        context = ApplicationProvider.getApplicationContext<Context>()
        val application = MyApp.instance
        val executors = Executors.newSingleThreadExecutor() // Example executor
        val userDao: UserDao = mock(UserDao::class.java)
        val mockUserDataSource = UserDataRepository(userDao)
        viewModel = LoginViewModel(userDao)
    }


  // TODO 3. I need to set up a fragment instance in another way.
 /*  @Test
    fun user_view_model_facebook_button_is_initialized() {

      Thread.sleep(sleepDuration)
        onView(withId(R.id.facebook_btn)).check(matches(isDisplayed()))

    }*/
}