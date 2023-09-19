package com.shawcroftstudios.ticketmastertakehome.helpers

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.domain.model.Images

class TestEventBuilder {
    companion object {
        fun createEvent(
            id: String = DEFAULT_ID,
            name: String = DEFAULT_NAME,
            city: String? = DEFAULT_CITY,
            venueName: String? = DEFAULT_VENUE_NAME,
            imageUrl: String? = DEFAULT_IMAGE_URL
        ): Event {
            return Event(id, name, city, venueName, Images(imageUrl, imageUrl))
        }

        private const val DEFAULT_ID = "testId"
        private const val DEFAULT_NAME = "testName"
        private const val DEFAULT_CITY = "testCity"
        private const val DEFAULT_VENUE_NAME = "testVenueName"
        private const val DEFAULT_IMAGE_URL = "https://www.example.com"
    }
}
