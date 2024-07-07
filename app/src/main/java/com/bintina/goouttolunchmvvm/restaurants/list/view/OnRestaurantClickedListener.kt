package com.bintina.goouttolunchmvvm.restaurants.list.view

import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser

interface OnRestaurantClickedListener {
    fun onRestaurantClick( restaurant: LocalRestaurant)
}