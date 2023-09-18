package com.shawcroftstudios.ticketmastertakehome.data.network

import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class EventApi @Inject constructor(retrofit: Retrofit) {

    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    suspend fun fetchEventsForCity(city: String): Result<EventsResponse> =
        try {
            val response = apiService.fetchEventsForCity(city)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (exception: IOException) {
            Result.failure(exception)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
}