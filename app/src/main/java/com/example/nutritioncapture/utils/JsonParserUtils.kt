package com.example.nutritioncapture.utils

import android.util.Log
import com.example.nutritioncapture.data.model.GeminiDishImageResponse
import kotlinx.serialization.json.Json


class JsonParserUtils {
    private val TAG = JsonParserUtils::class.simpleName

    fun parseGeminiImageResponse(dishesResult: String) : GeminiDishImageResponse {
        val obj = Json.decodeFromString<GeminiDishImageResponse>(dishesResult)

        obj.dishes?.let { dishes ->
            Log.d(TAG, dishes.joinToString(", "))
        }

        obj.ingredients?.let { ingredients ->
            Log.d(TAG, ingredients.joinToString(", "))
        }

        obj.calorie?.let { calorie ->
            Log.d(TAG, calorie.toString())
        }

        return obj
    }
}