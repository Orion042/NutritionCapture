package com.example.nutritioncapture.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.model.CardData
import com.example.nutritioncapture.data.model.UserInfo
import com.example.nutritioncapture.utils.imageToByteArray
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

suspend fun getDummyCardData(context: Context): List<CardData>? {
    delay(1500L)

    return listOf(
        CardData(id = 1, date = "2024/1/3", title = "Card 1", label = "Label 1", imageData = imageToByteArray(context, R.drawable.dummy_dish_image), description = "Description 1"),
        CardData(id = 2, date = "2024/1/2", title = "Card 2", label = "Label 2", imageData = createDummyByteArray(),description = "Description 2"),
        CardData(id = 3, date = "2024/1/1", title = "Card 3", label = "Label 3", imageData = createDummyByteArray(),description = "Description 3"),
    )
}

suspend fun getDummyUserData(context: Context) : List<UserInfo>? {
    delay(1000L)

    return listOf(
        UserInfo(id = 1, name = "Abcde", imageData = imageToByteArray(context, R.drawable.dummy_face_image)),
        UserInfo(id = 2, name = "Bcde", imageData = createDummyByteArray()),
        UserInfo(id = 3, name = "Cdef", imageData = createDummyByteArray()),
        UserInfo(id = 4, name = "Defg", imageData = createDummyByteArray()),
        UserInfo(id = 5, name = "Efgh", imageData = createDummyByteArray()),
        UserInfo(id = 6, name = "Fghi", imageData = createDummyByteArray())
    )
}

private fun createDummyByteArray() : ByteArray? {
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(Color.GRAY)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}