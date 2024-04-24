package com.bintina.goouttolunchmvvm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.user.login.view.MainActivity
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainInstrumentTests {

    @Rule
    @JvmField
    val rule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun background_views_present_correctly(){
        assertNotNull(withId(R.layout.activity_main))
        assertNotNull(withId(R.layout.fragment_login))
        assertNotNull(withId(R.id.login_container))
        //assertNotNull(withId(R.id.login_overlay_view))

    }

   /* @Test
    fun facebook_button_function_as_expected(){
        assertNotNull(withId(R.id.facebook_btn))
        onView(withId(R.id.facebook_btn)).perform(click())

    }

    @Test
    fun google_button_functions_as_expected(){
        assertNotNull(withId(R.id.google_login))
        onView(withId(R.id.google_login)).perform(click())

    }
*/
}