package com.example.nutritioncapture.utils

import android.graphics.Bitmap
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.example.nutritioncapture.BuildConfig
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

class GeminiUtils {
    private val TAG = GeminiUtils::class.simpleName

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.7f
            responseMimeType = "application/json"
        },
    )

    suspend fun getGeminiProResponseFromBitmap(bitmap: Bitmap) : String? {

        val prompt = """画像から以下の質問に対する回答を生成してください。回答言語は日本語にしてください。
                        1. 画像に映っている食べ物を指定した形式のdishesに入れてください。食べ物は複数ある可能性もあります。画像の隅々までチェックしてください。
                        2. 食べ物に含まれている食材を指定した形式のingredientsに入れてください。
                        3. 食べ物の推定カロリーを指定した形式のcalorieに入れてください。数値のみにしてください。
                        指定形式
                        Result = {'dishes': ArrayList<String>, 'ingredients': ArrayList<String>, 'calorie': Float}
                        Return: Result
                """.trimIndent()

        val inputContent = content {
            image(bitmap)
            text(prompt)
        }

        val response = try {
            val geminiResult = generativeModel.generateContent(inputContent)
            geminiResult.text
        } catch(ex: Exception) {
            Log.d(TAG, "Erro: $ex")
            "Error: $ex"
        }
        return response
    }
}