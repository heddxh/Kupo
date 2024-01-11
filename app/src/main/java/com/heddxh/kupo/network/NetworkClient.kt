package com.heddxh.kupo.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkClient {
    val client = HttpClient(Android) {
        install(ContentEncoding) {
            gzip()
        }
        install(Resources) // Type-safe requests
        install(Logging) { // Logging
            logger = KtorLogger()
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest {
            header("AcceptEncoding", "gzip")
        }
    }
}

/** Transfer Ktor logging to Android logcat */
class KtorLogger : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}