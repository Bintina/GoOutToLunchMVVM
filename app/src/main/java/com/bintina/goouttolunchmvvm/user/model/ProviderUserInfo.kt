package com.bintina.goouttolunchmvvm.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class ProviderUserInfo(
    val displayName: String,
    val email: String,
    val federatedId: String,
    val photoUrl: String,
    @PrimaryKey
    val providerId: String,
    val rawId: String
)