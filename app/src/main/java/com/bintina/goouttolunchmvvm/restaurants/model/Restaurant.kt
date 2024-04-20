package com.bintina.goouttolunchmvvm.restaurants.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val restaurantId: Int,
    val name: String,
    val locationUrl: String
)
