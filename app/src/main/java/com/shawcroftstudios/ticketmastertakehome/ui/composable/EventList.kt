package com.shawcroftstudios.ticketmastertakehome.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListUiState

@Composable
fun EventList(state: State<EventListUiState>) {
    val eventListUiState = state.value
    val eventItems = eventListUiState.eventItems
    val isLoading = eventListUiState.isLoading
    val errorMessage = eventListUiState.errorMessage

    if (isLoading) CircularProgressIndicator(modifier = Modifier.fillMaxSize(), color = Color.White)
    else if (errorMessage != null) {
        Text(text = errorMessage, color = Color.White)
    }

    DisplayEventItems(eventItems = eventItems)
}

@Composable
fun DisplayEventItems(eventItems: List<Event>) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = eventItems.isNotEmpty(),
        enter = slideInVertically {
            with(density) { -20.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        )
    ) {
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(
                eventItems.size
            ) { index ->
                EventItem(eventItems[index])
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}