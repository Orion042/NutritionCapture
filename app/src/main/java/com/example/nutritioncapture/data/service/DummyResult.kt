package com.example.nutritioncapture.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.entity.DishesEntity
import com.example.nutritioncapture.data.model.CardData
import com.example.nutritioncapture.data.model.UserInfo
import com.example.nutritioncapture.utils.imageToByteArray
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.util.Date

suspend fun getDummyCardData(context: Context): List<DishesEntity>? {
    delay(1500L)

    return listOf(
        DishesEntity(id = 1, dishesImageByteArrayString = createDummyByteArray()!!, dishesName = arrayListOf("sample"), dishesIngredients = arrayListOf("a", "b"), dishesCalorie = 1f, createdAt = Date()),
        DishesEntity(id = 2, dishesImageByteArrayString = createDummyByteArray()!!, dishesName = arrayListOf("sample"), dishesIngredients = arrayListOf("a", "b"), dishesCalorie = 1f, createdAt = Date()),
        DishesEntity(id = 3, dishesImageByteArrayString = createDummyByteArray()!!, dishesName = arrayListOf("sample"), dishesIngredients = arrayListOf("a", "b"), dishesCalorie = 1f, createdAt = Date()),
    )
}

suspend fun getDummyUserData(context: Context) : List<UserInfo>? {

    return listOf(
        UserInfo(id = "1", name = "Abcd", imageData = imageToByteArray(context, R.drawable.dummy_user_image01), Date(10L)),
        UserInfo(id = "2", name = "Bcde", imageData = imageToByteArray(context, R.drawable.dummy_user_image02), Date(11L)),
        UserInfo(id = "3", name = "Cdef", imageData = imageToByteArray(context, R.drawable.dummy_user_image03), Date(12L)),
        UserInfo(id = "4", name = "Defg", imageData = imageToByteArray(context, R.drawable.dummy_user_image04), Date(13L)),
        UserInfo(id = "5", name = "Fghi", imageData = imageToByteArray(context, R.drawable.dummy_user_image05), Date(15L))
    )
}

private fun createDummyByteArray() : ByteArray? {
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(Color.GRAY)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}