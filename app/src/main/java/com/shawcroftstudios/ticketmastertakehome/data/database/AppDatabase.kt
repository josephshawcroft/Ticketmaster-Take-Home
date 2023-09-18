package com.shawcroftstudios.ticketmastertakehome.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
