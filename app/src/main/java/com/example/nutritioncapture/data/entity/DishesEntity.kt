package com.example.nutritioncapture.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "dishes_table")
data class DishesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "dishes_image_array")
    val dishesImageByteArrayString: ByteArray,

    @ColumnInfo(name = "dishes_name")
    val dishesName: ArrayList<String>,

    @ColumnInfo(name = "dishes_ingredients")
    val dishesIngredients: ArrayList<String>,

    @ColumnInfo(name = "dishes_calorie")
    val dishesCalorie: Float,

    @ColumnInfo(name = "created_at")
    val createdAt: Date
)
