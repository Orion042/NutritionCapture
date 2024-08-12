package com.example.nutritioncapture.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun imageToByteArray(context: Context, drawableResId: Int): ByteArray {

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}