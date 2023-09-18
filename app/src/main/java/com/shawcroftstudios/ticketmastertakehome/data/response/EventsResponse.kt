package com.shawcroftstudios.ticketmastertakehome.data.response

import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("_embedded")
    val embedded: Embedded?,
)