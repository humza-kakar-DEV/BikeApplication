package com.example.smartbikefyp.model

data class MapRequestData(
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val type: String,
)
