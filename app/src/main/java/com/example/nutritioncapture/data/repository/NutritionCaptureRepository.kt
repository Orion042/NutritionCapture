package com.example.nutritioncapture.data.repository

import android.content.Context
import com.example.nutritioncapture.data.dao.DishesDao
import com.example.nutritioncapture.data.dao.UserInfoDao
import com.example.nutritioncapture.data.database.NutritionCaptureDatabase
import com.example.nutritioncapture.data.entity.DishesEntity
import com.example.nutritioncapture.data.entity.UserInfoEntity
import com.example.nutritioncapture.data.model.UserInfo

class NutritionCaptureRepository(context: Context) {
    private val userInfoDao: UserInfoDao by lazy { NutritionCaptureDatabase.getNutritionCaptureDatabaseInstance(context).userInfoDao }
    private val dishesDao: DishesDao by lazy { NutritionCaptureDatabase.getNutritionCaptureDatabaseInstance(context).dishesDao }

    suspend fun insertUserInfo(userInfo: UserInfoEntity) {
        userInfoDao.insertUserInfo(userInfo)
    }

    suspend fun getAllUserInfo() : List<UserInfoEntity> {
        return userInfoDao.getAllUserInfo()
    }

    suspend fun getUserInfoByUserId(id: String) : UserInfoEntity {
        return userInfoDao.getUserInfoById(id)
    }

    suspend fun getUserInfoByUserName(name: String) : UserInfoEntity {
        return userInfoDao.getUserInfoByName(name)
    }

    suspend fun getUserImageByteArrayByUserId(id: String) : ByteArray {
        return userInfoDao.getUserInfoImageByteArrayFromId(id)
    }

    suspend fun deleteUserInfo(userInfo: UserInfoEntity) {
        userInfoDao.deleteUserInfo(userInfo)
    }


    suspend fun insertDishes(dishes: DishesEntity) {
        dishesDao.insertDishes(dishes)
    }

    suspend fun getAllDishes() : List<DishesEntity> {
        return dishesDao.getAllDishes()
    }

    suspend fun getDishesByDishesId(id: Int) : DishesEntity {
        return dishesDao.getDishesByDishesId(id)
    }

    suspend fun getDishesImageByteArrayList() : List<ByteArray> {
        return dishesDao.getDishesImageArrayList()
    }

    suspend fun getDishesImageByteArrayByDishesId(id: Int) : ByteArray {
        return dishesDao.getDishesImageByteArrayByDishesId(id)
    }

    suspend fun deleteDishes(dishes: DishesEntity) {
        dishesDao.deleteDishes(dishes)
    }
}