package com.shawcroftstudios.ticketmastertakehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    TicketmasterTakeHomeTheme {
        Text("Hello Android!")
    }
}