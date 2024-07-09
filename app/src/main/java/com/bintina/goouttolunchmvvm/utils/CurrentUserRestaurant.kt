package com.bintina.goouttolunchmvvm.utils

import com.bintina.goouttolunchmvvm.user.model.LocalUser


data class CurrentUserRestaurant(
    val uid: String,
    val displayName: String,
    val address: String,
    val restaurantPictureUrl: String?,
    val restaurantId: String,
    val restaurantName: String,
    val latitude: Double,
    val longitude: Double,
    var attending: MutableList<LocalUser> = mutableListOf()
)
