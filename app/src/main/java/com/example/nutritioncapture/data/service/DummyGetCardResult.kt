package com.example.nutritioncapture.data.service

import android.graphics.Bitmap
import android.graphics.Color
import com.example.nutritioncapture.data.model.CardData
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

suspend fun getDummyCardDataFromRoom(): List<CardData>{
    delay(1500L)

    return listOf(
        CardData(id = 1, date = "2024/1/3", title = "Card 1", label = "Label 1", imageData = createDummyByteArray(), description = "Description 1"),
        CardData(id = 2, date = "2024/1/2", title = "Card 2", label = "Label 2", imageData = createDummyByteArray(),description = "Description 2"),
        CardData(id = 3, date = "2024/1/1", title = "Card 3", label = "Label 3", imageData = createDummyByteArray(),description = "Description 3"),
    )
}

private fun createDummyByteArray() : ByteArray? {
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(Color.GRAY)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}