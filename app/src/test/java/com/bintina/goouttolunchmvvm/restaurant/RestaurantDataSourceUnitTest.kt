package com.bintina.goouttolunchmvvm.restaurant

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.coroutineScope
import com.bintina.goouttolunchmvvm.mock.MainDispatcherRule
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.utils.MyApp.Companion.restaurantArrayList
import com.bintina.goouttolunchmvvm.utils.instantiateTodaysDate
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class RestaurantDataSourceUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry

    @get:Rule
    var rule: TestRule = RuleChain
        .outerRule(MainDispatcherRule(testDispatcher))
        .around(InstantTaskExecutorRule())

    @Before
    fun setup() {

        // Create a mock LifecycleOwner and its Lifecycle
        lifecycleOwner = mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Mock the Log.d method
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

/*    @Test
    fun datasource_results_filtered_for_null() = runBlocking {
        val mockRestaurants = MockRestaurants(4)
        val expectedResults = mockRestaurants.mockRestaurantList
        *//*val filteredMockRestaurants = filterRestaurantResult(expectedResults)

        // Assert
        assertEquals(filteredMockRestaurants.size, 1)*//*
    }*/


    @Test
    fun load_restaurants_method_returns_restaurant_class() = runBlocking {
        val dataSource = DataSource
        instantiateTodaysDate()

        dataSource.loadRestaurantList(lifecycleOwner.lifecycle.coroutineScope)
        assertTrue(restaurantArrayList is List<Restaurant?>)
    }

}
/*
    @Test
    fun `loadRestaurants handles API response correctly`() = runTest {
        // Arrange
        val mockApiClient = mock(ApiClient::class.java)
        val mockResponse = mock(RestaurantResult::class.java)
        val mockRestaurant = mock(Restaurant::class.java)
        val expectedRestaurants = listOf(mockRestaurant)

        *//*//*/ Mock DataSource to use mock data?
        val mockDataSource = object : DataSource() {
            override suspend fun loadRestaurants(lifecycleScope: LifecycleCoroutineScope): Deferred<List<Restaurant>> {
                return CompletableDeferred(expectedRestaurants)
            }
        }*//*

        whenever(mockResponse.results).thenReturn(expectedRestaurants)
        whenever(mockApiClient.getRestaurants()).thenReturn(mockResponse)

        // Act
        val deferredResult = DataSource.loadRestaurants(lifecycleOwner.lifecycle.coroutineScope)
        val result = deferredResult.await()

        // This test is expecting <[Mock for Restaurant, hashCode: 1648231985]>
        // Assert
        assertEquals(expectedRestaurants, result)
    }*/

   /* @Test
    fun `loadRestaurants handles API error correctly`() = runBlocking {
        // Arrange
        val mockApiClient = mock(ApiClient::class.java)
        whenever(mockApiClient.getRestaurants()).thenThrow(RuntimeException("API error"))

        // Use Dependency Injection or other methods to set ApiService.create() to return the mockApiService

        // Act & Assert
        try {
            val deferredResult = DataSource.loadRestaurants(lifecycleOwner.lifecycle.coroutineScope)
            deferredResult.await()
            assert(false) { "Expected an exception to be thrown" }
        } catch (e: Exception) {
            assertEquals("API error", e.message)
        }
    }*/