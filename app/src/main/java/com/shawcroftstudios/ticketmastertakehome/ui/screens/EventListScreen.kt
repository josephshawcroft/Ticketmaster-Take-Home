package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventList
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)

    Column {
        SearchBar { latestQuery ->
            viewModel.updateSearchQuery(latestQuery)
        }
        EventList(state = viewModel.eventListUiState)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchEventsForCity(HARDCODED_CITY)
    }
}

private const val HARDCODED_CITY = "Nottingham"
