package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant

interface OnAttendingClickedListener {

    fun onAttendingClick(currentUserRestaurant: CurrentUserRestaurant)
}
