package com.bintina.goouttolunchmvvm.user

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.matcher.ViewMatchers
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.coworkers.view.CoworkerListFragment
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CoworkersFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<CoworkerListFragment>

    @Before
    fun setup() {
        scenario = FragmentScenario.launchInContainer(CoworkerListFragment::class.java)
        instantiateTodaysDate()
    }

    @Test
    fun coworker_display_screen_views_exist(){
        Assert.assertNotNull(ViewMatchers.withId(R.id.coworker_recycler_container))
        Assert.assertNotNull(ViewMatchers.withId(R.id.ivWorkmateAvatar))

    }

}