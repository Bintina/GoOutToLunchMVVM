package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen

import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.CurrentUserRestaurant

interface OnAttendingClickedListener {

    fun onAttendingClick(currentUserRestaurant: CurrentUserRestaurant)
}
