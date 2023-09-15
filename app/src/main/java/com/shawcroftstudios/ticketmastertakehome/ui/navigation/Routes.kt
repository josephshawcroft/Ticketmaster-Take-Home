package com.shawcroftstudios.ticketmastertakehome.ui.navigation

sealed class Routes(val route: String) {
    object EventList : Routes("eventList")
}