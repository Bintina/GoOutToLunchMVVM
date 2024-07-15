package com.bintina.goouttolunchmvvm.restaurants.list.view

import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant

interface OnRestaurantClickedListener {
    fun onRestaurantClick( restaurant: LocalRestaurant)
}