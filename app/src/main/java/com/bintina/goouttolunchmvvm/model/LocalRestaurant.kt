package com.bintina.goouttolunchmvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["restaurantId"], unique = true)])
data class LocalRestaurant(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String = "",
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "address")
    val address: String = "",
    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String? = null,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = 0L,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = 0L,
    @ColumnInfo(name = "visited")
    var visited: Boolean = false
)