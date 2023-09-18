package com.shawcroftstudios.ticketmastertakehome.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,
    val name: String,
    val city: String = "Nottingham",
    val imageUrl: String?,
)