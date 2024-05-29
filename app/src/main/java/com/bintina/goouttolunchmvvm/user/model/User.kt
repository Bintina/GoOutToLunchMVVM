package com.bintina.goouttolunchmvvm.user.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Entity()
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 1,
    val name: String? = null,
    val loggedInWithGmail: Boolean = false,
    val loggedInWithFacebook: Boolean = false,
    val profilePicture: String? = null//,
    //var restaurant: Restaurant? = null

)
