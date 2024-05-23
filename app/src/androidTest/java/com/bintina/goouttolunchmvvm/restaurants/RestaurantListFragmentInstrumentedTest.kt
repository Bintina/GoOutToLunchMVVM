package com.bintina.goouttolunchmvvm.restaurants

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.restaurants.list.view.RestaurantListFragment
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import com.facebook.login.LoginFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RestaurantListFragmentInstrumentedTest {
    private lateinit var scenario: FragmentScenario<RestaurantListFragment>

    @Before
    fun setup() {
        scenario = FragmentScenario.launchInContainer(RestaurantListFragment::class.java)
        instantiateTodaysDate()
    }

    @Test
    fun restaurant_list_views_exist(){
        Assert.assertNotNull(ViewMatchers.withId(R.id.restaurant_recyclerView))
        Assert.assertNotNull(ViewMatchers.withId(R.id.card_view_restaurant))
    }
}