package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.google.android.gms.maps.model.LatLng


@Entity
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
    val photoUrl: String?,
    @ColumnInfo(name = "attending")
    var attending: String = "",
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = 0L,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = 0L,
    @ColumnInfo(name = "visited")
    val visited: Boolean = false
)
