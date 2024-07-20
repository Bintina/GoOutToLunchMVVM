package com.bintina.goouttolunchmvvm.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["uid"], unique = true)])
data class LocalUser(
    @PrimaryKey(autoGenerate = false)
    val uid: String = "",
    @ColumnInfo(name = "display_name")
    val displayName: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = 0L,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = 0L
)