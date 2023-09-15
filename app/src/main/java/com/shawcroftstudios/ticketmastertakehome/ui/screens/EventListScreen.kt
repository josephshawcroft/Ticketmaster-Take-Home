package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)

    // TODO add UI here
    Greeting("EventListScreen shown")
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
