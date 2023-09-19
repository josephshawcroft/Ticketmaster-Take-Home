package com.shawcroftstudios.ticketmastertakehome.ui.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shawcroftstudios.ticketmastertakehome.R
import com.shawcroftstudios.ticketmastertakehome.data.DataResult
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.domain.usecase.GetEventsForCityUsecase
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val usecase: GetEventsForCityUsecase,
) : ViewModel() {

    private val _eventListUiState = mutableStateOf(
        EventListUiState(
            isLoading = true,
            eventItems = emptyList(),
        )
    )
    val eventListUiState: State<EventListUiState> get() = _eventListUiState

    private val _searchQuery = mutableStateOf("")

    fun fetchEventsForCity(city: String) {
        viewModelScope.launch {

            val useCaseFlow = usecase.execute(city)

            useCaseFlow.collectLatest { result ->
                val state = when (result) {
                    is DataResult.Success -> {
                        EventListUiState(isLoading = false, eventItems = result.data)
                    }
                    is DataResult.Loading -> EventListUiState(isLoading = true)
                    is DataResult.Error -> EventListUiState(
                        isLoading = false,
                        errorMessageResourceId = extractErrorResource(result.exception)
                    )
                }
                _eventListUiState.value = state
                if (result is DataResult.Success) updateFilteredEvents(_searchQuery.value)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch { updateFilteredEvents(query) }
    }

    private suspend fun updateFilteredEvents(query: String) {
        val eventListUiState = _eventListUiState.value

        val filteredEvents = withContext(dispatcher.default) {
            val events = eventListUiState.eventItems

            if (query.isBlank()) {
                events
            } else {
                events.filter { it.name.contains(query, ignoreCase = true) }
            }
        }

        _eventListUiState.value = eventListUiState.copy(filteredEventItems = filteredEvents)
    }

    @StringRes
    private fun extractErrorResource(throwable: Throwable): Int =
        if (throwable is NoAvailableEventsException) {
            R.string.no_events_available_to_show_at_this_time
        } else R.string.unknown_error_has_occurred

}

data class EventListUiState(
    val isLoading: Boolean = false,
    val eventItems: List<Event> = emptyList(),
    val filteredEventItems: List<Event> = emptyList(),
    val errorMessageResourceId: Int? = null,
)