package com.example.smartbikefyp.model.map

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val business_status: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    var permanently_closed: Boolean? = null,
    var photos: List<Photo>? = null,
    val place_id: String,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val vicinity: String
)