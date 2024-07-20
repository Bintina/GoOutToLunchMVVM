package com.bintina.goouttolunchmvvm.user

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.user.coworkers.view.CoworkerListFragment
import com.bintina.goouttolunchmvvm.user.coworkers.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.model.database.repositories.MockUser
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CoworkersFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<CoworkerListFragment>
private lateinit var mockUser: MockUser
    @Before
    fun setup() {
        mockUser = MockUser(4)
        scenario = FragmentScenario.launchInContainer(CoworkerListFragment::class.java)
        instantiateTodaysDate()
    }

    @Test
    fun coworker_display_screen_views_exist(){
        Assert.assertNotNull(ViewMatchers.withId(R.id.coworker_recycler_container))
        Assert.assertNotNull(ViewMatchers.withId(R.id.ivWorkmateAvatar))

    }

    @Test
    fun testRecyclerViewItemCount() {
        onView(withId(R.id.coworker_recycler_container)).check(matches(hasChildCount(mockUser.size)))
    }

    @Test
    fun testFirstItemData() {
        onView(withId(R.id.coworker_recycler_container))
            .perform(RecyclerViewActions.scrollToPosition<Adapter.ItemViewHolder>(0))
        onView(withText("Albert")).check(matches(isDisplayed()))
        onView(withText("albert@example.com")).check(matches(isDisplayed()))
    }
}