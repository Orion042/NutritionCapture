package com.example.nutritioncapture.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoImageViewModel : ViewModel() {

    var imageBitMap by mutableStateOf<ByteArray?>(null)
        private set

    fun setImageByteArray(byteArray: ByteArray?) {
        viewModelScope.launch {
            byteArray.let {
                imageBitMap = byteArray
            }
        }
    }
}