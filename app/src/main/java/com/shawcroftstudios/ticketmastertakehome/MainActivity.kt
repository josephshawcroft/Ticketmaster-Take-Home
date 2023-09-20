package com.shawcroftstudios.ticketmastertakehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shawcroftstudios.ticketmastertakehome.ui.navigation.AppNavigation
import com.shawcroftstudios.ticketmastertakehome.ui.theme.TicketmasterTakeHomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketmasterTakeHomeTheme {
                AppNavigation(owner = this@MainActivity)
            }
        }
    }
}