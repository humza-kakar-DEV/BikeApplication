package com.example.smartbikefyp.model

data class ItemBoughtService(
    var buyerId: String = "",
    var creatorId: String = "",
    var buyerUser: User? = null,
    var creatorUser: User? = null,
    var boughtDate: BoughtDate? = null,
    var service: Service? = null,
)
