package com.shawcroftstudios.ticketmastertakehome.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events.json?")
    suspend fun fetchEventsByCity(
        @Query("city") name: String,
    ): Response<Any>
}