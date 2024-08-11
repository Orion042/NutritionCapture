package com.example.nutritioncapture.data.model

data class CardData(
    val id: Int,
    val date: String,
    val title: String,
    val label: String,
    val imageData: ByteArray?,
    val description: String
)
