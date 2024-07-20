package com.bintina.goouttolunchmvvm.restaurants.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.model.LocalRestaurant

@Entity
class LocalRestaurantsList(
    @PrimaryKey(autoGenerate = false)
    val uid: Int = 0,
    @ColumnInfo(name = "restaurants_list")
    val restaurantsList: List<LocalRestaurant>,
    @ColumnInfo(name = "list_size")
    val listSize: Int) {
}