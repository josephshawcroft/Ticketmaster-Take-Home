package com.shawcroftstudios.ticketmastertakehome.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier
            .testTag("CARD_ITEM_TEXT_TAG")
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
            ) {
                // todo add image here
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(text = event.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = event.name, fontSize = 14.sp, color = Color.LightGray)
            }
        }
    }
}

@Preview
@Composable
fun EventItemPreview() = EventItem(
    event = Event(
        "1",
        "Slayer @ Motorpoint Arena",
        null,
        null,
    )
)
