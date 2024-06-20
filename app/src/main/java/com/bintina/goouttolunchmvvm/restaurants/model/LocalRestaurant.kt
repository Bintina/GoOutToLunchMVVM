package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LocalRestaurant(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String,
    val name: String,
    //val location: Location,
    val photoUrl: String?
//    val numberOfInterestedUsers: Int?
    //val visited: Boolean
)
