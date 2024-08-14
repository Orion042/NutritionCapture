package com.example.nutritioncapture.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoImageViewModel : ViewModel() {

    var imageByteArrayMutableState by mutableStateOf<ByteArray?>(null)

    fun setImageByteArray(byteArray: ByteArray?) {
        viewModelScope.launch {
            byteArray.let {
                imageByteArrayMutableState = byteArray
            }
        }
    }
}