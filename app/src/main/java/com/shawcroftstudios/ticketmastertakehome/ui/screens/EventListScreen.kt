package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventList
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner, windowSizeClass: WindowSizeClass) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)

    Column {
        SearchBar { latestQuery ->
            viewModel.updateSearchQuery(latestQuery)
        }
        EventList(uiState = viewModel.eventListUiState, windowSizeClass = windowSizeClass) {
            viewModel.fetchEventsForCity(HARDCODED_CITY) // 'pull to refresh' callback
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchEventsForCity(HARDCODED_CITY)
    }
}

// TODO mention testing multiple hardcoded cities
private const val HARDCODED_CITY = "Nottingham"
