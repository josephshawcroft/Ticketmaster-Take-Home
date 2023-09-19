package com.shawcroftstudios.ticketmastertakehome.di

import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapperImpl
import com.shawcroftstudios.ticketmastertakehome.data.repository.LocalEventListRepository
import com.shawcroftstudios.ticketmastertakehome.data.repository.LocalEventListRepositoryImpl
import com.shawcroftstudios.ticketmastertakehome.data.repository.RemoteEventListRepository
import com.shawcroftstudios.ticketmastertakehome.data.repository.RemoteEventListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEventMapper(eventMapperImpl: EventMapperImpl): EventMapper

    @Binds
    abstract fun bindLocalEventListRepository(localEventListRepository: LocalEventListRepositoryImpl): LocalEventListRepository

    @Binds
    abstract fun bindRemoteEventListRepository(remoteEventListRepository: RemoteEventListRepositoryImpl): RemoteEventListRepository
}
