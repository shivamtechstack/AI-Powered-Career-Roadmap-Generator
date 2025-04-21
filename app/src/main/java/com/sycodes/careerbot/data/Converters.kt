package com.sycodes.careerbot.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun toStringList(json: String): List<String> =
        Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
}
