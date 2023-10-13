package com.example.smartbikefyp.repository

import android.content.Context
import android.net.Uri
import com.example.plantsservicefyp.MapRequest
import com.example.smartbikefyp.getMimeType
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.model.MapRequestData
import com.example.smartbikefyp.model.Product
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.model.Service
import com.example.smartbikefyp.model.ServiceUser
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.model.map.GoogleMapRequestModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StoreImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val mapRequest: MapRequest,
) : Store {

    override fun createProduct(product: Product, imageUri: Uri) = flow<Nothing> {
        product.apply {
            val imageDownloadUri = firebaseStorage
                .reference
                .child("images/${System.currentTimeMillis()}.${context.getMimeType(imageUri)}")
                .putFile(imageUri)
                .await()
                .storage
                .downloadUrl
                .await()

            this.productId = firebaseFirestore.collection("product").document().id
            this.imageUri = imageDownloadUri.toString()

            firebaseFirestore
                .collection("product")
                .document(this.productId)
                .set(this)
                .await()
        }
    }

    override fun createService(service: Service, imageUri: Uri) = flow<Nothing> {
        service.apply {
            val imageDownloadUri = firebaseStorage
                .reference
                .child("images/${System.currentTimeMillis()}.${context.getMimeType(imageUri)}")
                .putFile(imageUri)
                .await()
                .storage
                .downloadUrl
                .await()

            context.log("image service url: ${imageDownloadUri.toString()}")

            this.serviceId = firebaseFirestore.collection("service").document().id
            this.imageUri = imageDownloadUri.toString()

            firebaseFirestore
                .collection("service")
                .document(this.serviceId)
                .set(this)
                .await()

            context.log("service data uploaded")
        }
    }

    override val getProductCount: Flow<Int> = flow {
        emit(
            firebaseFirestore.collection("product")
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count
                .toInt()
        )
    }

    override val getServiceCount: Flow<Int> = flow {
        emit(
            firebaseFirestore.collection("service")
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count
                .toInt()
        )
    }

    override fun getProductWithCategory(category: String) = callbackFlow<Product> {
        val snapshotListener =
            firebaseFirestore.collection("product")
                .whereEqualTo("productCategory", category)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                        snapshot.toObjects(Product::class.java).map {
                            trySend(it)
                        }
                    }
                }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getProductWithUser(product: Product) = callbackFlow<ProductUser> {
        val snapshotListener =
            firebaseFirestore.collection("user_info")
                .whereEqualTo("userId", product.creatorId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                        snapshot.toObjects(User::class.java).map {
                            trySend(ProductUser(product, it))
                        }
                    }
                }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getServiceWithCategory(category: String) = callbackFlow<Service> {
        val snapshotListener =
            firebaseFirestore.collection("service")
                .whereEqualTo("serviceCategory", category)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                        snapshot.toObjects(Service::class.java).map {
                            trySend(it)
                        }
                    }
                }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getServiceWithUser(service: Service) = callbackFlow<ServiceUser> {
        val snapshotListener =
            firebaseFirestore.collection("user_info")
                .whereEqualTo("userId", service.creatorId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                        snapshot.toObjects(User::class.java).map {
                            trySend(ServiceUser(service, it))
                        }
                    }
                }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun buyProduct(itemBoughtProduct: ItemBoughtProduct) = flow<Boolean> {
        firebaseFirestore
            .collection("bought_product")
            .document()
            .set(itemBoughtProduct)
            .await()
    }

    override fun buyService(itemBoughtService: ItemBoughtService) = flow<Boolean> {
        firebaseFirestore
            .collection("bought_service")
            .document()
            .set(itemBoughtService)
            .await()
    }

    override fun getBoughtProductsWithBuyerId(buyerId: String) =
        callbackFlow<List<ItemBoughtProduct>> {
            if (firebaseFirestore.collection("bought_product").get().await().isEmpty) {
                trySend(emptyList())
            }
            val snapshotListener =
                firebaseFirestore.collection("bought_product")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                            val data = mutableListOf<ItemBoughtProduct>()
                            if (snapshot.documents.isNotEmpty()) {
                                snapshot.documents.forEach {
                                    data.add(it.toObject(ItemBoughtProduct::class.java)!!)
                                }
                                trySend(data)
                            } else {
                                context.log("products are empty")
                                trySend(emptyList())
                            }
                        }
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override fun getBoughtProductsWithCreatorId(creatorId: String) =
        callbackFlow<List<ItemBoughtProduct>> {
            if (firebaseFirestore.collection("bought_product").get().await().isEmpty) {
                trySend(emptyList())
            }
            val snapshotListener =
                firebaseFirestore.collection("bought_product")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                            val data = mutableListOf<ItemBoughtProduct>()
                            if (snapshot.documents.isNotEmpty()) {
                                snapshot.documents.forEach {
                                    data.add(it.toObject(ItemBoughtProduct::class.java)!!)
                                }
                                trySend(data)
                            } else {
                                context.log("products are empty")
                                trySend(emptyList())
                            }
                        }
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override fun getBoughtServicesWithCreatorId(creatorId: String) =
        callbackFlow<List<ItemBoughtService>> {
            if (firebaseFirestore.collection("bought_service").get().await().isEmpty) {
                trySend(emptyList())
            }
            val snapshotListener =
                firebaseFirestore.collection("bought_service")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                            val data = mutableListOf<ItemBoughtService>()
                            if (snapshot.documents.isNotEmpty()) {
                                snapshot.documents.forEach {
                                    data.add(it.toObject(ItemBoughtService::class.java)!!)
                                }
                                trySend(data)
                            } else {
                                context.log("services are empty")
                                trySend(emptyList())
                            }
                        }
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override fun getBoughtServicesWithBuyerId(buyerId: String) =
        callbackFlow<List<ItemBoughtService>> {
            if (firebaseFirestore.collection("bought_service").get().await().isEmpty) {
                trySend(emptyList())
            }
            val snapshotListener =
                firebaseFirestore.collection("bought_service")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                            val data = mutableListOf<ItemBoughtService>()
                            snapshot.documents.forEach {
                                data.add(it.toObject(ItemBoughtService::class.java)!!)
                            }
                            trySend(data)
                        }
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override fun googleMapsNearBy(mapRequestData: MapRequestData): Flow<GoogleMapRequestModel> = flow<GoogleMapRequestModel> {
        emit(mapRequest.nearByPlaces(mapRequestData))
    }

}




























