package com.example.nutritioncapture.viewmodel

import androidx.lifecycle.ViewModel

class ViewModelOwner {
    companion object {
        val photoImageViewModel = PhotoImageViewModel()
    }

    fun getPhotoImageViewModel() : PhotoImageViewModel {
        return photoImageViewModel
    }
}