package com.bintina.goouttolunchmvvm.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class UserX(
    val createdAt: String,
    val displayName: String,
    val email: String,
    val emailVerified: Boolean,
    val lastLoginAt: String,
    val lastRefreshAt: String,
    @PrimaryKey
    val localId: String,
    val photoUrl: String,
    val providerUserInfo: List<ProviderUserInfo>,
    val validSince: String
)