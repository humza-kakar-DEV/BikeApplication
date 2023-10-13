package com.example.smartbikefyp

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object KtorClient {

    @OptIn(ExperimentalSerializationApi::class)
    val httpClient = HttpClient(Android){

        install(HttpTimeout){
            socketTimeoutMillis = 30000
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }

        install(Logging){

            logger = object: Logger {
                override fun log(message: String) {
                    Log.d("TAG", "log: $message")
                }
            }

        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = kotlinx.serialization.json.Json {
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    encodeDefaults= true
                    ignoreUnknownKeys = true
                    isLenient = true
//                    coerceInputValues = true
                }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

    }

}