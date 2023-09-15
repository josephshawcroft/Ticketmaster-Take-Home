package com.shawcroftstudios.ticketmastertakehome.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

}