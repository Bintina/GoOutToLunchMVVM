package com.bintina.goouttolunchmvvm.login.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val loginState: Int,
    val profilePicture: String,
    var restaurant: String?
)
