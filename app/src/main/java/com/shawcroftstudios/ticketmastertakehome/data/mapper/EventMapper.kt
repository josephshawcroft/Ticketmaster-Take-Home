package com.shawcroftstudios.ticketmastertakehome.data.mapper

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse

interface EventMapper {

    fun mapToDomain(eventsResponse: EventsResponse) : List<Event>
}