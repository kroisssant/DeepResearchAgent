package org.david.Search

import org.david.ApiClient.IApiClient
import org.david.ApiClient.OkHttpApiClient
import org.david.Json.JsonConfig

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