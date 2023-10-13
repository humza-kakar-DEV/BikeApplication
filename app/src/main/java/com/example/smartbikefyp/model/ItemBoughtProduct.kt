package com.example.smartbikefyp.model

data class ItemBoughtProduct(
    val buyerId: String = "",
    val creatorId: String = "",
    val buyerUser: User? = null,
    val creatorUser: User? = null,
    val product: Product? = null,
)
