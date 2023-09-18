package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventList
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)
    var events: List<Event> = emptyList()

    Column {
        SearchBar(viewModel)
        EventList(state = viewModel.eventListUiState)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchEventsForCity("Nottingham")
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
