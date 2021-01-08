package com.example.triviaapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


class Converters {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<String?>? {
        if (data == null) {
            return null
        }
        val listType =
            object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson<ArrayList<String?>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<String?>?): String? {
        return gson.toJson(someObjects)
    }
}