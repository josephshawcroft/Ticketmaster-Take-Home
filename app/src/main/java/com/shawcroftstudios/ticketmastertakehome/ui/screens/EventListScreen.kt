package com.shawcroftstudios.ticketmastertakehome.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventList
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListUiState
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListViewModel

@Composable
fun EventListScreen(viewModelStoreOwner: ViewModelStoreOwner) {

    val viewModel = hiltViewModel<EventListViewModel>(viewModelStoreOwner)

    EventListScreenUi(
        viewModel.eventListUiState,
        { viewModel.fetchEventsForCity(HARDCODED_CITY) },
        viewModel::updateSearchQuery
    )

    LaunchedEffect(Unit) {
        viewModel.fetchEventsForCity(HARDCODED_CITY)
    }
}

@Composable
fun EventListScreenUi(
    state: State<EventListUiState>,
    onPullRefresh: () -> Unit,
    onSearchQueryUpdate: (String) -> Unit
) {

    Column {
        SearchBar { latestQuery ->
            onSearchQueryUpdate(latestQuery)
        }
        EventList(state) {
            onPullRefresh()
        }
    }
}

@Composable
@Preview
fun EventListScreenWithItems() {

    val state = remember {
        mutableStateOf(
            EventListUiState(
                isLoading = false,
                filteredEventItems = listOf(
                    Event(
                        "testId",
                        "testName",
                        "testCity",
                        "testVenueName",
                        null
                    ),
                    Event(
                        "testId2",
                        "testName2",
                        "testCity2",
                        "testVenueName",
                        null

                    ),
                    Event(
                        "testId3",
                        "testName3",
                        "testCity3",
                        "testVenueName",
                        null
                    )
                )
            )
        )
    }

    EventListScreenUi(state, {}, {})
}

private const val HARDCODED_CITY = "Nottingham"