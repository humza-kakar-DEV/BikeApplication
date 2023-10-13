package com.example.smartbikefyp.repository

import android.net.Uri
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.model.MapRequestData
import com.example.smartbikefyp.model.Product
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.model.Service
import com.example.smartbikefyp.model.ServiceUser
import com.example.smartbikefyp.model.map.GoogleMapRequestModel
import kotlinx.coroutines.flow.Flow

interface Store {
    val getProductCount: Flow<Int>
    val getServiceCount: Flow<Int>
    fun createProduct(product: Product, imageUri: Uri): Flow<Nothing>
    fun createService(service: Service, imageUri: Uri): Flow<Nothing>
    fun getProductWithCategory(category: String): Flow<Product>
    fun getProductWithUser(product: Product): Flow<ProductUser>
    fun getBoughtProductsWithBuyerId(buyerId: String): Flow<List<ItemBoughtProduct>>
    fun getBoughtProductsWithCreatorId(creatorId: String): Flow<List<ItemBoughtProduct>>
    fun getServiceWithCategory(category: String): Flow<Service>
    fun getServiceWithUser(service: Service): Flow<ServiceUser>
    fun getBoughtServicesWithBuyerId(buyerId: String): Flow<List<ItemBoughtService>>
    fun getBoughtServicesWithCreatorId(creatorId: String): Flow<List<ItemBoughtService>>
    fun buyService(itemBoughtService: ItemBoughtService): Flow<Boolean>
    fun buyProduct(itemBoughtProduct: ItemBoughtProduct): Flow<Boolean>
    fun googleMapsNearBy(mapRequestData: MapRequestData): Flow<GoogleMapRequestModel>
}