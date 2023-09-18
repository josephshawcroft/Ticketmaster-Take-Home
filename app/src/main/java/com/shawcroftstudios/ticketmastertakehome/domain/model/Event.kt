package com.shawcroftstudios.ticketmastertakehome.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,
    val name: String,
    val date: String?,
    val imageUrl: String?,
)