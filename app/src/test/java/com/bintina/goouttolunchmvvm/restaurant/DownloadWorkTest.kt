package com.bintina.goouttolunchmvvm.restaurant

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder

import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Geometry
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Location
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Northeast
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.OpeningHours
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Photo
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.PlusCode
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Southwest
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Viewport
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
                        "OPERATIONAL",
                        Geometry(
                            Location(
                                -4.308492,
                                39.5808522
                            ),
                            Viewport(
                                Northeast(
                                    -4.306733170107277,
                                    39.58215147989272
                                ),
                                Southwest(
                                    -4.309432829892721,
                                    39.57945182010727
                                )
                            )
                        ),
                        "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                        "#FF9E67",
                        "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                        "Asha Bistro",
                        OpeningHours(
                            true
                        ),
                        listOf(
                            Photo(
                                3008,
                                listOf(
                                    "<a href=\"https://maps.google.com/maps/contrib/100116944439636469333\">Thomas Kößler</a>"
                                ),
                                "AUGGfZkKawz04Mn8TNvMynPtqT20ioVV-fv2QhchDKbyX4Zx4fbzVENja7JG6aND8IfuO8VCY7LnGYXJUlb3iiNJJ6aPXwZd1m0LtDNuwnHuVpvnSnNpq4YXv35HlQx6I9N8QVZfp3zHpNCtfI_xlK-Iws1l0LjE5zrfVKHSlg2jKhLJ31rn",
                                4000
                            )
                        ),
                        "ChIJBQZH9UhJQBgRqKDlUOBtZz8",
                        PlusCode(
                            "MHRJ+J8 Diani Beach",
                            "6G7XMHRJ+J8"
                        ),
                        2,
                        4.6,
                        "ChIJBQZH9UhJQBgRqKDlUOBtZz8",
                        "GOOGLE",
                        listOf(
                            "restaurant",
                            "food",
                            "point_of_interest",
                            "establishment"
                        ),
                        328,
                        "Diani Beach"
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
