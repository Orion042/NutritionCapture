package com.example.nutritioncapture.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.nutritioncapture.data.entity.DishesEntity

@Dao
interface DishesDao {
    @Insert
    suspend fun insertDishes(dishes: DishesEntity)

    @Query("SELECT * FROM dishes_table")
    suspend fun getAllDishes() : List<DishesEntity>

    @Query("SELECT * FROM dishes_table WHERE id = :id")
    suspend fun getDishesByDishesId(id: Int) : DishesEntity

    @Query("SELECT dishes_image_array FROM dishes_table")
    suspend fun getDishesImageArrayList() : List<ByteArray>

    @Query("SELECT dishes_image_array FROM dishes_table WHERE id = :id")
    suspend fun getDishesImageByteArrayByDishesId(id: Int) : ByteArray

    @Delete
    suspend fun deleteDishes(dishes: DishesEntity)
}