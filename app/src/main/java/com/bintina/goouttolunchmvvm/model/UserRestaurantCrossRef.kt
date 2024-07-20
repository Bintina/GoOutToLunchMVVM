package com.bintina.goouttolunchmvvm.model

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "restaurantId"])
data class UserRestaurantCrossRef(
    val uid: String = "",
    val restaurantId: String = ""
)