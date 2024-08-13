package com.example.nutritioncapture.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import java.io.ByteArrayOutputStream

fun imageToByteArray(context: Context, drawableResId: Int): ByteArray {

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun imageToByteArray(image: ImageProxy): ByteArray {
    val buffer = image.planes[0].buffer
    val byteArray = ByteArray(buffer.remaining())
    buffer.get(byteArray)
    return byteArray
}

@Composable
fun BitmapViewer(bitmap: Bitmap) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                setImageBitmap(bitmap)
            }
        }
    )
}