package com.bintina.goouttolunchmvvm.restaurants

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.LiveDataTestUtil
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.SaveRestaurantDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RestaurantViewModelInstrumentedTest {
    private var database: SaveRestaurantDatabase? = null

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            SaveRestaurantDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb(){
        database?.close()
    }

    private val RESTAURANT_ID: Long = 10
    private val RESTAURANT_DEMO =
        Restaurant(RESTAURANT_ID,
            "YummyPalace",
            "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2",
            5,
            true
            )
    @Test
    @Throws(InterruptedException::class)
    fun insertRestaurant(){
        //Before: Adding a new user
        this.database?.restaurantDao()?.getRestaurant(RESTAURANT_DEMO.restaurantId)
        //Test
        val restaurant =
            LiveDataTestUtil.getValue(this.database?.restaurantDao()?.getRestaurant(RESTAURANT_ID))
        Assert.assertTrue(restaurant.name == RESTAURANT_DEMO.name && restaurant.restaurantId == RESTAURANT_DEMO.restaurantId)

    }
}