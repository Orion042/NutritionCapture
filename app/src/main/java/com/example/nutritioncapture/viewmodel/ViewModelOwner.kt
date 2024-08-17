package com.example.nutritioncapture.viewmodel

import androidx.lifecycle.ViewModel

class ViewModelOwner {
    companion object {
        val photoImageViewModel = PhotoImageViewModel()
        val dishesViewModel = DishesViewModel()
        val searchViewModel = SearchViewModel()
    }

    fun getPhotoImageViewModel() : PhotoImageViewModel {
        return photoImageViewModel
    }

    fun getDishesViewModel() : DishesViewModel {
        return dishesViewModel
    }

    fun getSearchViewModel() : SearchViewModel {
        return searchViewModel
    }
}