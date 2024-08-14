package com.example.nutritioncapture.viewmodel

import androidx.lifecycle.ViewModel

class ViewModelOwner {
    companion object {
        val photoImageViewModel = PhotoImageViewModel()
        val dishesViewModel = DishesViewModel()
    }

    fun getPhotoImageViewModel() : PhotoImageViewModel {
        return photoImageViewModel
    }

    fun getDishesViewModel() : DishesViewModel {
        return dishesViewModel
    }
}