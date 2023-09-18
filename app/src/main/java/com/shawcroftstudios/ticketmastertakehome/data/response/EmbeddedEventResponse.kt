package com.shawcroftstudios.ticketmastertakehome.data.response

import com.google.gson.annotations.SerializedName

data class EmbeddedEventResponse(
    val id: String?,
    val dates: Dates?,
    val images: List<Image>?,
    val name: String?,
    @SerializedName("_embedded")
    val embedded: EmbeddedVenues?,
)

data class EmbeddedVenues(
    val venues: List<Venue>?,
)
data class Venue(
    val name: String?,
    val city: City?
)
data class City(val name: String?)