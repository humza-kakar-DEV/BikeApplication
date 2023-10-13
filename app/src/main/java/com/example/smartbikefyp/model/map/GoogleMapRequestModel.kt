package com.example.smartbikefyp.model.map

import kotlinx.serialization.Serializable

@Serializable
data class GoogleMapRequestModel(
    val html_attributions: List<String>,
    val next_page_token: String? = null,
    val results: List<Result>,
    val status: String
)