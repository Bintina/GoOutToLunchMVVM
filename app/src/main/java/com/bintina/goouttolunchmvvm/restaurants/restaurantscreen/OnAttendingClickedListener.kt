package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser

interface OnAttendingClickedListener {

    fun onAttendingClick(currentUser: LocalUser, restaurant: LocalRestaurant)
}
