package org.example.ApiClient

interface IApiClient {
    fun postRequest(endpoint: String, jsonBody: String): String
    fun getRequest(endpoint: String): String
    fun putRequest(endpoint: String, jsonBody: String): String
    fun deleteRequest(endpoint: String): String
}