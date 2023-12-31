package com.shawcroftstudios.ticketmastertakehome.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shawcroftstudios.ticketmastertakehome.R
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

@Composable
fun EventItem(event: Event, modifier: Modifier = Modifier) {

    val current = LocalConfiguration.current
    val width = current.screenWidthDp.dp

    // dp values derived from: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
    // additional note: would tidy this up and abstract this away given more time
    val imageUrl = if (width >= 600.dp) {
        event.imageUrl?.tabletImageUrl
    } else event.imageUrl?.phoneImageUrl

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = imageUrl,
                    error = painterResource(id = R.drawable.placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = event.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .testTag(EVENT_ITEM_NAME_TEST_TAG)
                )
                Text(
                    text = event.venueName ?: stringResource(R.string.venue_tba),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .testTag(EVENT_ITEM_VENUE_TEST_TAG)
                )
            }
        }
    }
}

@Preview
@Composable
fun EventItemPreviewAllDataPresent() = EventItem(
    event = Event(
        "1",
        "Slayer @ Motorpoint Arena",
        "Nottingham",
        "Motorpoint Arena",
        null
    )
)

@Preview
@Composable
fun EventItemPreviewNoVenueName() = EventItem(
    event = Event(
        "1",
        "Slayer coming to Nottingham!",
        "Nottingham",
        null,
        null
    )
)

const val EVENT_ITEM_NAME_TEST_TAG = "EVENT_ITEM_NAME_TEST_TAG"
const val EVENT_ITEM_VENUE_TEST_TAG = "EVENT_ITEM_VENUE_TEST_TAG"