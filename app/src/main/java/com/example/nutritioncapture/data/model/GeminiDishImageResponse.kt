package com.example.nutritioncapture.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiDishImageResponse(
    val dishes: ArrayList<String>?,
    val ingredients: ArrayList<String>?,
    val calorie: Float?
)
