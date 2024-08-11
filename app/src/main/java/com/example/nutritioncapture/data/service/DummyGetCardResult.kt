package com.example.nutritioncapture.data.service

import com.example.nutritioncapture.data.model.CardData
import kotlinx.coroutines.delay

suspend fun getDummyCardDataFromRoom(): List<CardData>{
    delay(500L)

    return listOf(
        CardData(id = 1, date = "2024/1/3", title = "Card 1", label = "Label 1", description = "Description 1"),
        CardData(id = 2, date = "2024/1/2", title = "Card 2", label = "Label 2", description = "Description 2"),
        CardData(id = 3, date = "2024/1/1", title = "Card 3", label = "Label 3", description = "Description 3"),
    )
}