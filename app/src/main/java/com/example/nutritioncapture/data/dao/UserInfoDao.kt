package com.example.nutritioncapture.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.nutritioncapture.data.entity.UserInfoEntity
import com.example.nutritioncapture.data.model.UserInfo

@Dao
interface UserInfoDao {
    @Insert
    suspend fun insertUserInfo(userInfo: UserInfoEntity)

    @Query("SELECT * FROM user_info_table")
    suspend fun getAllUserInfo(): List<UserInfoEntity>

    @Query("SELECT * FROM user_info_table WHERE id = :id")
    suspend fun getUserInfoById(id: String): UserInfoEntity

    @Query("SELECT * FROM user_info_table WHERE name = :name")
    suspend fun getUserInfoByName(name: String): UserInfoEntity

    @Query("SELECT image_byte_array FROM user_info_table WHERE id = :id")
    suspend fun getUserInfoImageByteArrayFromId(id: String): ByteArray

    @Delete
    suspend fun deleteUserInfo(userInfo: UserInfoEntity)
}