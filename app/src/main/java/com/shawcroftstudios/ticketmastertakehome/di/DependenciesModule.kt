package com.shawcroftstudios.ticketmastertakehome.di

import com.shawcroftstudios.ticketmastertakehome.utils.DefaultDispatcherProvider
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DependenciesModule {

    @Binds
    abstract fun bindDispatcherProvider(dispatcherProvider: DefaultDispatcherProvider) : DispatcherProvider
}