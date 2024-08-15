package com.example.nutritioncapture.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_info_table")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_byte_array")
    val imageByteArray: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Date
)
