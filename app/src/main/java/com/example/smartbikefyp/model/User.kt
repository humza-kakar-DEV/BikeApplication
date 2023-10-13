package com.example.smartbikefyp.model

import android.net.Uri

data class User(
    var userId: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var yourAddress: String = "",
    var shopAddress: String = "",
    var imageUri: String = "",
    var email: String = "",
    var password: String = "",
    var role: String = "",
)
