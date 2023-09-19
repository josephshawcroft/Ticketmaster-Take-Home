package com.shawcroftstudios.ticketmastertakehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shawcroftstudios.ticketmastertakehome.ui.navigation.AppNavigation
import com.shawcroftstudios.ticketmastertakehome.ui.theme.TicketmasterTakeHomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            TicketmasterTakeHomeTheme {
                AppNavigation(owner = this@MainActivity, windowSizeClass)
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