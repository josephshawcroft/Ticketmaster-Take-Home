package com.shawcroftstudios.ticketmastertakehome.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shawcroftstudios.ticketmastertakehome.domain.usecase.GetEventsForCityUsecase
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val usecase: GetEventsForCityUsecase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            usecase.execute("Nottingham")
        }
    }
}