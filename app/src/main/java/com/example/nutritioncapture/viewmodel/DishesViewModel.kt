package com.example.nutritioncapture.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritioncapture.data.entity.DishesEntity
import com.example.nutritioncapture.data.repository.NutritionCaptureRepository
import com.example.nutritioncapture.utils.compressImageToJPEG
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date

@SuppressLint("MutableCollectionMutableState")
class DishesViewModel : ViewModel() {
    private val TAG = DishesViewModel::class.simpleName

    private val _dishesName = mutableStateOf<ArrayList<String>>(ArrayList())
    val dishesName: State<ArrayList<String>> get() = _dishesName

    private val _dishesIngredients = mutableStateOf<ArrayList<String>>(ArrayList())
    val dishesIngredients: State<ArrayList<String>> get() = _dishesIngredients

    private val _dishesCalorie = mutableStateOf<Float?>(0f)
    val dishesCalorie: State<Float?> get() = _dishesCalorie


    fun setDishesName(dishesNameArrayList: ArrayList<String>?) {
        viewModelScope.launch {
            dishesNameArrayList.let {
                _dishesName.value = dishesNameArrayList!!
            }
        }
    }

    fun setDishesIngredients(ingredientsArrayList: ArrayList<String>?) {
        viewModelScope.launch {
            ingredientsArrayList.let {
                _dishesIngredients.value = ingredientsArrayList!!
            }
        }
    }

    fun setDishesCalorie(calorie: Float?) {
        viewModelScope.launch {
            calorie.let {
                _dishesCalorie.value = calorie
            }
        }
    }

    fun resetViewModel() {
        _dishesName.value = ArrayList()
        _dishesIngredients.value = ArrayList()
        _dishesCalorie.value = 0f
    }

    fun saveDishesResult(repository: NutritionCaptureRepository) {
        val currentDateTime = Date()

        val compressedByteArray = compressImageToJPEG(ViewModelOwner().getPhotoImageViewModel().imageByteArrayMutableState!!, 50)

        viewModelScope.launch {
            try {
                repository.insertDishes(DishesEntity(dishesImageByteArrayString = compressedByteArray, dishesName = _dishesName.value, dishesIngredients = _dishesIngredients.value, dishesCalorie = _dishesCalorie.value!!, createdAt = currentDateTime))
                Log.d(TAG, "DB保存成功")
            } catch (ex: Exception) {
                Log.d(TAG, "DB保存失敗\nERROR: ${ex.message}")
            }
        }
    }
}