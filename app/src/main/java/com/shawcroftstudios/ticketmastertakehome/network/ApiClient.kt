package com.shawcroftstudios.ticketmastertakehome.network

import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ApiClient @Inject constructor(retrofit: Retrofit) {

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    suspend fun fetchEventsByCity(city: String): Response<Any> =
        apiService.fetchEventsByCity(name = city)
}