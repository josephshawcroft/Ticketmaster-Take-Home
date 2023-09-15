package com.shawcroftstudios.ticketmastertakehome.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatcherProvider {

    val main: CoroutineDispatcher
        get() = Dispatchers.Main
    val default: CoroutineDispatcher
        get() = Dispatchers.Default
    val io: CoroutineDispatcher
        get() = Dispatchers.IO
    val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider
