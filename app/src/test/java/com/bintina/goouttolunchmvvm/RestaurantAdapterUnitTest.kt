package com.bintina.goouttolunchmvvm

import com.bintina.goouttolunchmvvm.mock.repository.MockRestaurants
import com.bintina.goouttolunchmvvm.restaurants.list.view.OnRestaurantClickedListener
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class RestaurantAdapterUnitTest {

    @MockK(relaxed = true)
    lateinit var listener: OnRestaurantClickedListener

    lateinit var adapter: Adapter

    @Before
    fun set_up(){
        MockKAnnotations.init(this)
        adapter = Adapter()
        adapter.listener = listener
    }

    val mockRestaurants = MockRestaurants(4)
    val expectedResults = mockRestaurants.mockRestaurantList

    @After
    fun tear_down(){
        clearAllMocks()
    }

    @Test
    fun get_item_count_should_return_correct_item_count(){
        adapter.restaurantList = expectedResults

        val count = adapter.itemCount

        assert(count == 4)
    }

    @Test
    fun on_bind_view_holder_should_bind_data_to_view_holder(){
        val viewHolder: Adapter.ItemViewHolder = mockk(relaxed = true)
        val position = 0
        val restaurant: Restaurant = mockk(relaxed = true)

        adapter.restaurantList = listOf(restaurant)
        adapter.onBindViewHolder(viewHolder, position)

        verify(exactly = 1) {viewHolder.bind(restaurant, listener)}
    }

}