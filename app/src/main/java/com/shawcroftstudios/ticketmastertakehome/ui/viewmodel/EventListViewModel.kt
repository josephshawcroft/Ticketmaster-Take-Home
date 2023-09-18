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

    private val _eventListUiState = mutableStateOf(
        EventListUiState(
            isLoading = false,
            eventItems = emptyList(),
        )
    )
    val eventListUiState: State<EventListUiState> get() = _eventListUiState

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun fetchEventsForCity(city: String) {
        _eventListUiState.value = EventListUiState(isLoading = true)
        viewModelScope.launch {
            val result = usecase.execute(city)
            val response = result.getOrNull()
            _eventListUiState.value =
                EventListUiState(isLoading = false, eventItems = response.orEmpty())
            updateFilteredEvents(_searchQuery.value) // force the filtering based on what's currently in the search bar
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredEvents(query)
    }

    private fun updateFilteredEvents(query: String) {
        val eventListUiState = _eventListUiState.value
        val events = eventListUiState.eventItems

        val filteredEvents = if (query.isBlank()) {
            events
        } else {
            events.filter { it.name.contains(query) }
        }

        _eventListUiState.value = eventListUiState.copy(filteredEventItems = filteredEvents)
    }

}

data class EventListUiState(
    val isLoading: Boolean = false,
    val eventItems: List<Event> = emptyList(),
    val filteredEventItems: List<Event> = emptyList(),
    val errorMessage: String? = null,
)