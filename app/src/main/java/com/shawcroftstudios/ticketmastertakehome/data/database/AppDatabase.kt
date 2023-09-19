package com.shawcroftstudios.ticketmastertakehome.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

@Database(entities = [Event::class], version = 1)
@TypeConverters(ImagesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
