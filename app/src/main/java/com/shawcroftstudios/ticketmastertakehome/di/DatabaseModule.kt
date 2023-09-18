package com.shawcroftstudios.ticketmastertakehome.di

import android.content.Context
import androidx.room.Room
import com.shawcroftstudios.ticketmastertakehome.data.database.AppDatabase
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventDao(database: AppDatabase): EventDao {
        return database.eventDao()
    }
}

private const val DATABASE_NAME = "events"