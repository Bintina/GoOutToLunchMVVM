package com.bintina.goouttolunchmvvm.user.model


import androidx.room.Entity
import androidx.room.PrimaryKey

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
