package com.shawcroftstudios.ticketmastertakehome.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,
    val name: String,
    val city: String?,
    val venueName: String?,
    val imageUrl: Images?,
)

data class Images(
    val phoneImageUrl: String?,
    val tabletImageUrl: String?
)