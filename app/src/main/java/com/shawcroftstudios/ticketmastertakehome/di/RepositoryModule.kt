package com.shawcroftstudios.ticketmastertakehome.di

import com.shawcroftstudios.ticketmastertakehome.data.repository.EventListRepository
import com.shawcroftstudios.ticketmastertakehome.data.repository.EventListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEventListRepository(eventListRepositoryImpl: EventListRepositoryImpl): EventListRepository

}
