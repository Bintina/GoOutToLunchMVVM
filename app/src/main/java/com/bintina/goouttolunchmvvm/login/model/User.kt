package com.bintina.goouttolunchmvvm.login.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant

@Entity()
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 1,
    val name: String? = null,
    val loginState: Int = 0,
    val profilePicture: String? = null,

    var restaurant: Restaurant? = null

)
