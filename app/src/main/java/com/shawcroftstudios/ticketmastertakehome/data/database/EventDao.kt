package com.shawcroftstudios.ticketmastertakehome.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE city = :city")
    suspend fun getEventsForCity(city: String): List<Event>

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
}
