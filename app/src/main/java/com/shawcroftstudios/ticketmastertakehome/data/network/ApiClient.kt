package com.shawcroftstudios.ticketmastertakehome.data.network

import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ApiClient @Inject constructor(retrofit: Retrofit) {

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    suspend fun fetchEventsByCity(city: String): Response<EventsResponse> =
        apiService.fetchEventsByCity(name = city)
}