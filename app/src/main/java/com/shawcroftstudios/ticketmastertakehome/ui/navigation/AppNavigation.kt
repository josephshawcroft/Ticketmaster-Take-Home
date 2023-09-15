package com.shawcroftstudios.ticketmastertakehome.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shawcroftstudios.ticketmastertakehome.ui.screens.EventListScreen

@Composable
fun AppNavigation(owner: ViewModelStoreOwner) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.EventList.route) {
        composable(route = Routes.EventList.route) {
            EventListScreen(owner)
        }
    }
}
