package com.example.plantsservicefyp
import com.example.smartbikefyp.KtorClient
import com.example.smartbikefyp.model.MapRequestData
import com.example.smartbikefyp.model.map.GoogleMapRequestModel
import io.ktor.client.request.*
import javax.inject.Inject

class MapRequest @Inject constructor() {

//    use maps when you only want attributes
//    if you want to filter fields than use filter

    suspend fun nearByPlaces(
        mapRequestData: MapRequestData
    ): GoogleMapRequestModel =
        KtorClient.httpClient.post {
            url("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            parameter("location", "${mapRequestData.latitude},${mapRequestData.longitude}")
            parameter("radius", mapRequestData.radius)
            parameter("type", mapRequestData.type)
            parameter("key", "AIzaSyClx2Nq7wTukyWBobIm7jAG_j-7B_BVoPM")
        }

}