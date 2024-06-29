package com.bintina.goouttolunchmvvm.user.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant

@Entity()
data class LocalUser(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String? = null,
    var attending: LocalRestaurant?,
    val createdAt: Long,
    val updatedAt: Long
    //var restaurant: Restaurant? = null

){


}
