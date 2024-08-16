package com.example.nutritioncapture.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPOutputStream

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

fun bitmapToBase64(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): String {

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(format, quality, outputStream)

    val byteArray = outputStream.toByteArray()

    return Base64.getEncoder().encodeToString(byteArray)
}

fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    return scaledBitmap
}

fun resizeByteArrayImageWidthAndHeight(byteArray: ByteArray, newWidth: Int, newHeight: Int): ByteArray {
    val originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)

    return outputStream.toByteArray()
}

fun compressAndEncodeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(format, quality, outputStream)
    val byteArray = outputStream.toByteArray()

    // 圧縮
    val compressedByteArray = ByteArrayOutputStream().use { bos ->
        GZIPOutputStream(bos).use { gzip ->
            gzip.write(byteArray)
        }
        bos.toByteArray()
    }

    return Base64.getEncoder().encodeToString(compressedByteArray)
}

fun compressImageToJPEG(byteArray: ByteArray, quality: Int): ByteArray {
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
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