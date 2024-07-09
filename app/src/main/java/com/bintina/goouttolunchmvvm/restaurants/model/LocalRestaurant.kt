package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.google.android.gms.maps.model.LatLng


@Entity
data class LocalRestaurant(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String?,
    @ColumnInfo(name = "attending")
    var attending: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long,
    @ColumnInfo(name = "visited")
    val visited: Boolean
)
