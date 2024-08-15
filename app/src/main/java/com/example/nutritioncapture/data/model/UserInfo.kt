package com.example.nutritioncapture.data.model

import java.util.Date

data class UserInfo(
    val id: String,
    val name: String,
    val imageData: ByteArray?,
    val createdAt: Date
)
