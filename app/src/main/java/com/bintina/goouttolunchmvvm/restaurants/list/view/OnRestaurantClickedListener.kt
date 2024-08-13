package com.bintina.goouttolunchmvvm.restaurants.list.view

import com.bintina.goouttolunchmvvm.model.LocalRestaurant

interface OnRestaurantClickedListener {
    fun onRestaurantClick( restaurant: LocalRestaurant)
}