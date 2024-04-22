package com.bintina.goouttolunchmvvm.login.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Entity()
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 1,
    val name: String? = null,
    val loginState: Int = 0,
    val profilePicture: String? = null//,
    //var restaurant: Restaurant? = null

)
