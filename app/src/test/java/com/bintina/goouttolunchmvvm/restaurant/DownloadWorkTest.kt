/*
package com.bintina.goouttolunchmvvm.restaurant

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker

import androidx.work.ListenableWorker.Result
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.work.CustomWorkerFactory
import com.bintina.goouttolunchmvvm.restaurants.work.DownloadWork
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DownloadWorkTest {

    private lateinit var context: Context

    @Mock
    private lateinit var dataSource: DataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = ApplicationProvider.getApplicationContext()
    }
        val worker = TestListenableWorkerBuilder<DownloadWork>(
            context = context)
            .setWorkerFactory(CustomWorkerFactory(dataSource)) // Ensure CustomWorkerFactory is correctly set up
            .build()

    @Test
    fun testDoWork_success() {

        // Mock the DataSource loadRestaurantList method
        runBlocking {
            Mockito.`when`(dataSource.loadRestaurantList(Mockito.any())).thenReturn(
                listOf(
                    Restaurant(
                        place_id = "1",
                        name = "Test Restaurant",
                        vicinity = "123 Test St",
                        geometry = Restaurant.Geometry(Restaurant.Location(0.0, 0.0)),
                        photos = emptyList()
                    )
                )
            )
        }

        // Perform the work
        val result = worker.startWork().get()

        // Verify the result is success
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun testDoWork_failure() {

        // Mock the DataSource loadRestaurantList method to throw an exception
        runBlocking {
            Mockito.`when`(dataSource.loadRestaurantList(Mockito.any()))
                .thenThrow(RuntimeException("Error loading data"))
        }

        // Perform the work
        val result = worker.startWork().get()

        // Verify the result is failure
        assertEquals(ListenableWorker.Result.failure(), result)
    }

    @Test
    fun testGetPlacesRestaurantList_empty() {

        // Mock the DataSource loadRestaurantList method to return an empty list
        runBlocking {
            Mockito.`when`(dataSource.loadRestaurantList(Mockito.any()))
                .thenReturn(emptyList<Restaurant>())
        }

        // Perform the work
        val result = worker.getPlacesRestaurantList() // Ensure this method returns the list

        // Verify the result
        assertEquals(emptyList<Restaurant>(), result)
    }
}
*/
