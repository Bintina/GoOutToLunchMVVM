package com.bintina.goouttolunchmvvm.user

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import com.facebook.login.LoginFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<LoginFragment>

    @Before
    fun setup() {
        scenario = FragmentScenario.launchInContainer(LoginFragment::class.java)
        instantiateTodaysDate()
    }

    @Test
    fun login_screen_views_exist(){
        Assert.assertNotNull(ViewMatchers.withId(R.id.login_container))
        Assert.assertNotNull(ViewMatchers.withId(R.id.facebook_btn))
        Assert.assertNotNull(ViewMatchers.withId(R.id.google_login_btn))
    }
}