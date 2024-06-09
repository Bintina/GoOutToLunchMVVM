package com.bintina.goouttolunchmvvm.user.model

import androidx.room.Entity

@Entity()
data class UserResponse(
    val kind: String,
    val users: List<UserX>
)