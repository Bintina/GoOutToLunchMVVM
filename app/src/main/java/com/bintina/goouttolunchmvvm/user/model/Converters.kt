package com.bintina.goouttolunchmvvm.user.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromProviderUserInfoList(value: List<ProviderUserInfo>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<ProviderUserInfo>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProviderUserInfoList(value: String): List<ProviderUserInfo>? {
        val gson = Gson()
        val type = object : TypeToken<List<ProviderUserInfo>>() {}.type
        return gson.fromJson(value, type)
    }
}