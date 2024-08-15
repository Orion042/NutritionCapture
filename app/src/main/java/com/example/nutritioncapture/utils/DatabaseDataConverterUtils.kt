package com.example.nutritioncapture.utils

import androidx.room.TypeConverter
import java.util.Base64
import java.util.Date

class DatabaseDataConverterUtils {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time
    }

    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): String? {
        return byteArray?.let { Base64.getEncoder().encodeToString(it) }
    }

    @TypeConverter
    fun toByteArray(encodedString: String?): ByteArray? {
        return encodedString?.let { Base64.getDecoder().decode(it) }
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        return ArrayList(value.split(", ").map { it.trim() })
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        return list.joinToString(", ")
    }
}