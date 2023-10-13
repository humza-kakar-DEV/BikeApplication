package com.example.smartbikefyp.model

import android.net.Uri

data class Service(
    var serviceId: String = "",
    var creatorId: String = "",
    var imageUri: String = "",
    var name: String = "",
    var description: String = "",
    var price: String = "",
    var serviceCategory: String = "",
)
