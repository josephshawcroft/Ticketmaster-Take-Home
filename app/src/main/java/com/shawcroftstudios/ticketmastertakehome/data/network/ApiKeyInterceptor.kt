package com.shawcroftstudios.ticketmastertakehome.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder()
            .addQueryParameter(API_KEY, apiKey)
            .build()

        val requestWithApiKey = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(requestWithApiKey)
    }

    private companion object {

        private const val API_KEY = "apikey"
    }
}
