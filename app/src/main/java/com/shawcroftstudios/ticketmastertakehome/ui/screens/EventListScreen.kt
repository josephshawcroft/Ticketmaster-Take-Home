package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventList
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)

    SearchBar()
    EventList(state = viewModel.eventList)
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
