package com.shawcroftstudios.ticketmastertakehome.data.mapper

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import javax.inject.Inject

class EventMapperImpl @Inject constructor() : EventMapper {

    override fun mapToDomain(eventsResponse: EventsResponse): List<Event> {

        val events = eventsResponse.embedded?.events ?: return emptyList()
        return events.mapNotNull {
            val id = it.id
            val name = it.name
//            val city = it.city TODO

            if (id != null && name != null) {
                Event(id, name, "Nottingham", it.dates?.start?.localDate, it.images?.firstOrNull()?.url)
            } else null
        }
    }
}