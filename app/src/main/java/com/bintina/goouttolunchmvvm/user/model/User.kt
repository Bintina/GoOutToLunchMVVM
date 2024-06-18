package com.bintina.goouttolunchmvvm.user.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Entity()
data class User(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val displayName: String,
    val email: String,
    val profilePictureUrl: String? = null//,
    //var restaurant: Restaurant? = null

){


}
