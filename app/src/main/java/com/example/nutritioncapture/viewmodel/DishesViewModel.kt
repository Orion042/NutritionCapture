package com.example.nutritioncapture.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
class DishesViewModel : ViewModel() {
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
}