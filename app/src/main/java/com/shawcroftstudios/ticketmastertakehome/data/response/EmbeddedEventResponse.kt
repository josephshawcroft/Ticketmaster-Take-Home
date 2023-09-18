package com.shawcroftstudios.ticketmastertakehome.data.response

data class EmbeddedEventResponse(
    val id: String?,
    val dates: Dates?,
    val images: List<Image>?,
    val name: String?,
)