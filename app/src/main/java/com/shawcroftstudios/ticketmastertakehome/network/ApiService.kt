package com.shawcroftstudios.ticketmastertakehome.network

import com.shawcroftstudios.ticketmastertakehome.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("?")
    suspend fun fetchEventsByCity(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("city") name: String,
    ): Response<Any>
}