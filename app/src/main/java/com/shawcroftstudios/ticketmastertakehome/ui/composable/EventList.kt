package com.shawcroftstudios.ticketmastertakehome.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shawcroftstudios.ticketmastertakehome.ui.viewmodel.EventListUiState

/**
 *     'Pull to refresh' implementation- refresh state is based on VM loading state.
 *     Wanted to have it in as it's a nicer UX and a pretty cool Compose feature
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventList(uiState: State<EventListUiState>, onPullRefresh: () -> Unit) {
    val density = LocalDensity.current
    val filteredEventItems = uiState.value.filteredEventItems

    var refreshing by remember { mutableStateOf(false) }

    refreshing = uiState.value.isLoading
    val state = rememberPullRefreshState(refreshing, onPullRefresh)

    val errorMessage = uiState.value.errorMessageResourceId?.let { stringResource(id = it) }

    AnimatedVisibility(
        visible = filteredEventItems.isNotEmpty() || errorMessage != null,
        enter = slideInVertically {
            with(density) { -20.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        )
    ) {
        Box(
            Modifier.pullRefresh(state)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(
                    if (errorMessage != null) 1 // this approach allows pull to refresh to still be enabled when an error is shown
                    else filteredEventItems.size
                ) { index ->
                    if (errorMessage != null) Text(text = errorMessage, color = Color.Black)
                    else EventItem(filteredEventItems[index])
                    Spacer(Modifier.height(4.dp))
                }
            }
            PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
        }
    }
}