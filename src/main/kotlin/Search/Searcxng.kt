package org.example.Search

import org.example.ApiClient.IApiClient
import org.example.ApiClient.OkHttpApiClient
import org.example.Json.JsonConfig

class Searcxng: Browser {
    val client: IApiClient = OkHttpApiClient()
    val url = "http://localhost:8888/search"
    override fun search(query: String): List<SearchFound> {
        // Implement search logic here
        val res = client.getRequest(
            "$url?q=$query&format=json"
        )

        return JsonConfig.default.decodeFromString<SearchResult>(res).results
    }
}