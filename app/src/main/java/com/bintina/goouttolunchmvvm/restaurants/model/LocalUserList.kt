package com.bintina.goouttolunchmvvm.restaurants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bintina.goouttolunchmvvm.user.model.LocalUser
@Entity
class LocalUserList(
    @PrimaryKey(autoGenerate = false)
    val uid: Int,
    @ColumnInfo(name = "coworkers_list")
    val coworkersList: List<LocalUser>,
    @ColumnInfo(name = "list_size")
    val listSize: Int) {
}