package com.bintina.goouttolunchmvvm.restaurants.model

import android.arch.persistence.room.PrimaryKey

data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val restaurantId: Int,
    val name: String,
    val locationUrl: String,
    val photoUrl: String?,
    val numberOfInterestedUsers: Int?,
    val visited: Boolean
)
