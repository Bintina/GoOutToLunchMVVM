package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.user.model.LocalUser


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["uid"],
            childColumns = ["currentUserUid"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["display_name"],
            childColumns = ["currentUserName"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["currentUserUid"]),
        Index(value = ["currentUserName"])
    ]
)
data class LocalRestaurant(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String = "",
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "address")
    val address: String = "",
    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String? = null,
    @ColumnInfo(name = "attendingList")
    var attendingList: String = "",
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = 0L,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = 0L,
    @ColumnInfo(name = "visited")
    val visited: Boolean = false,
    @ColumnInfo(name = "currentUserName", defaultValue = "")
    var currentUserName: String = "",
    @ColumnInfo(name = "currentUserUid", defaultValue = "")
    var currentUserUid: String = "",
    @ColumnInfo(name = "currentUserAttendingBoolean", defaultValue = "false")
    var currentUserAttendingBoolean: Boolean = false
)
