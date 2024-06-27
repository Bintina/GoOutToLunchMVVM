package com.bintina.goouttolunchmvvm.restaurants

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.LiveDataTestUtil
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant

import com.bintina.goouttolunchmvvm.utils.AppDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class RestaurantViewModelInstrumentedTest {
    private var database: AppDatabase? = null

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database?.close()
    }

    // Dummy date and time: December 31, 2022, 23:59:59
    val dummyUpdateDate = LocalDateTime.of(2022, 12, 31, 23, 59, 59)
    val updatedAt = dummyUpdateDate.toEpochSecond(ZoneOffset.UTC)
    // Dummy date and time: December 31, 2023, 23:59:59
    val dummyCreatedDate = LocalDateTime.of(2023, 12, 31, 23, 59, 59)
    val createdAt = dummyCreatedDate.toEpochSecond(ZoneOffset.UTC)
    private val RESTAURANT_ID: String = "10"
    private val RESTAURANT_DEMO =
        LocalRestaurant(
            RESTAURANT_ID,
            "YummyPalace",
            "Airstrip Road One",
            -4.3015359,
            39.5744260,
            "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2",
            0,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    @Test
    @Throws(InterruptedException::class)
    fun insertRestaurant() {
        //Before: Adding a new user
        this.database?.restaurantDao()?.getRestaurant(RESTAURANT_DEMO.restaurantId)
        //Test
        val restaurant =
            LiveDataTestUtil.getValue(this.database?.restaurantDao()?.getRestaurant(RESTAURANT_ID))
        Assert.assertTrue(restaurant.name == RESTAURANT_DEMO.name && restaurant.restaurantId == RESTAURANT_DEMO.restaurantId)

    }
}