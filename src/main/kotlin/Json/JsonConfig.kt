package org.example.Json

import kotlinx.serialization.json.Json

object JsonConfig {
    val default = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = false
    }
}