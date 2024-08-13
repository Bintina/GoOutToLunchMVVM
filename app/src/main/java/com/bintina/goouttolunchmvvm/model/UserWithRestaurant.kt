package com.bintina.goouttolunchmvvm.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithRestaurant(
    @Embedded val user: LocalUser? = LocalUser(),
    @Relation(
        parentColumn = "uid",
        entityColumn = "restaurantId",
        associateBy = Junction(UserRestaurantCrossRef::class)
    )
    var restaurant: LocalRestaurant? =LocalRestaurant()
)