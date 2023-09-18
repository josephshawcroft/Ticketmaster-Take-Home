package com.shawcroftstudios.ticketmastertakehome.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
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

    private val _eventList = mutableStateOf(
        EventListUiState(
            isLoading = false,
            eventItems = emptyList(),
        )
    )
    val eventList: State<EventListUiState> get() = _eventList

    init {
        viewModelScope.launch {
            usecase.execute("Nottingham")
        }
    }
}

data class EventListUiState(
    val isLoading: Boolean = false,
    val eventItems: List<Event> = emptyList(),
    val errorMessage: String? = null,
)