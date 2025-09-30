package org.david.ApiClient

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class OkHttpApiClient: IApiClient {
    val client = OkHttpClient.Builder().connectTimeout(0, TimeUnit.MINUTES).webSocketCloseTimeout(0, TimeUnit.MILLISECONDS).readTimeout(0,
        TimeUnit.MILLISECONDS).writeTimeout(0, TimeUnit.MILLISECONDS).build()
    override fun postRequest(endpoint: String, jsonBody: String): String {
        val req = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .post(req)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            return response.body.string()
        }

    }

    override fun getRequest(endpoint: String): String {
        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            return response.body.string()
        }
    }

    override fun putRequest(endpoint: String, jsonBody: String): String {
        val req = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .put(req)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            return response.body.string()
        }
    }

    override fun deleteRequest(endpoint: String): String {
        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .delete()
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            return response.body.string()
        }
    }
}