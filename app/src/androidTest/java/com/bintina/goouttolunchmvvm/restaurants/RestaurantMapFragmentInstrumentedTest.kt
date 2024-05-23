package com.bintina.goouttolunchmvvm.restaurants

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RestaurantMapFragmentInstrumentedTest {
    private lateinit var scenario: FragmentScenario<com.bintina.goouttolunchmvvm.restaurants.map.view.RestaurantsMapFragment>

    @Before
    fun setup() {
        scenario = FragmentScenario.launchInContainer(com.bintina.goouttolunchmvvm.restaurants.map.view.RestaurantsMapFragment::class.java)
        instantiateTodaysDate()
    }

    @Test
    fun restaurant_map_fragment_views_display (){
        Assert.assertNotNull(ViewMatchers.withId(R.id.search_autocomplete_fragment))
        Assert.assertNotNull(ViewMatchers.withId(R.id.map))
    }
}