package com.shawcroftstudios.ticketmastertakehome.data.mapper

import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import com.shawcroftstudios.ticketmastertakehome.data.response.Image
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import javax.inject.Inject

class EventMapperImpl @Inject constructor() : EventMapper {

    override fun mapToDomain(eventsResponse: EventsResponse): List<Event> {

        val events = eventsResponse.embedded?.events ?: return emptyList()
        return events.mapNotNull {
            val id = it.id
            val name = it.name
            val venue = it.embedded?.venues?.firstOrNull()
            val venueName = venue?.name
            val city = venue?.city?.name

            val url = it.images?.let { images -> determineImageUrlToUse(images) }
            if (id != null && name != null) {
                Event(id, name, city, venueName, url)
            } else null
        }
    }

    private fun determineImageUrlToUse(images: List<Image>): String? {
        val image = images.filter { it.ratio == "3_2" }.minByOrNull { it.height ?: Int.MAX_VALUE }
        return image?.url
    }
}