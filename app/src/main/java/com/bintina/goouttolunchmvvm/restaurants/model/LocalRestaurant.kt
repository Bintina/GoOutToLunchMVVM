package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val attending: Int = 0
    //val visited: Boolean
)
