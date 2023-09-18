package com.shawcroftstudios.ticketmastertakehome.data.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OKHttp interceptor class used to append the API key to all queries
 * While not particularly useful when doing just 1 API call, this would prove to be scalable
 * when adding more API calls to the codebase as we eliminate the need for boilerplate API key code
 */
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
