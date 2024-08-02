package com.bintina.goouttolunchmvvm.restaurants.list.view

import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.RestaurantWithUsers

interface OnRestaurantClickedListener {
    fun onRestaurantClick( restaurant: RestaurantWithUsers)
}