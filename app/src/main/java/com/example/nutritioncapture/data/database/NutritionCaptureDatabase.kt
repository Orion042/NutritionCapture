package com.example.nutritioncapture.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nutritioncapture.data.dao.DishesDao
import com.example.nutritioncapture.data.dao.UserInfoDao
import com.example.nutritioncapture.data.entity.DishesEntity
import com.example.nutritioncapture.data.entity.UserInfoEntity
import com.example.nutritioncapture.utils.DatabaseDataConverterUtils

@Database(entities = [UserInfoEntity::class, DishesEntity::class], version = 1, exportSchema = true)
@TypeConverters(DatabaseDataConverterUtils::class)
abstract class NutritionCaptureDatabase: RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
    abstract val dishesDao: DishesDao

    companion object {
        @Volatile
        private var INSTANCE: NutritionCaptureDatabase? = null

        fun getNutritionCaptureDatabaseInstance(context: Context) : NutritionCaptureDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NutritionCaptureDatabase::class.java,
                        "nutritionCapture_database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
            return instance
            }
        }
    }
}