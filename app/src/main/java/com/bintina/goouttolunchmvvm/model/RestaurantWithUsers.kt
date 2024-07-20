package com.bintina.goouttolunchmvvm.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RestaurantWithUsers(
    @Embedded val restaurant: LocalRestaurant = LocalRestaurant(),
    @Relation(
        parentColumn = "restaurantId",
        entityColumn = "uid",
        associateBy = Junction(UserRestaurantCrossRef::class)
    )
    val users: List<LocalUser>
)