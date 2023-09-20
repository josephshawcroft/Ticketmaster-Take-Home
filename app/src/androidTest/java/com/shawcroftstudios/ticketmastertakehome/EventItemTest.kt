package com.shawcroftstudios.ticketmastertakehome

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EVENT_ITEM_NAME_TEST_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EVENT_ITEM_VENUE_TEST_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.EventItem
import com.shawcroftstudios.ticketmastertakehome.ui.theme.TicketmasterTakeHomeTheme
import org.junit.Rule
import org.junit.Test

class EventItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val eventNameTag: String = EVENT_ITEM_NAME_TEST_TAG
    private val eventVenueTag: String = EVENT_ITEM_VENUE_TEST_TAG

    @Test
    fun eventItemWithVenueTest() {
        val name = "testName"
        val venue = "testVenueName"

        val event = Event("testId", name, "testCity", venue, null)

        composeTestRule.setContent {
            TicketmasterTakeHomeTheme {
                EventItem(event = event, true)
            }
        }

        composeTestRule.onNodeWithTag(eventNameTag).assert(hasText(name))
        composeTestRule.onNodeWithTag(eventVenueTag).assert(hasText(venue))
    }

    @Test
    fun eventItemWithUnspecifiedVenueTest() {
        val name = "testName"
        lateinit var expectedVenue : String

        val event = Event("testId", name, "testCity", null, null)

        composeTestRule.setContent {
            expectedVenue = stringResource(id = R.string.venue_tba)

            TicketmasterTakeHomeTheme {
                EventItem(event = event, true)
            }
        }

        composeTestRule.onNodeWithTag(eventNameTag).assert(hasText(name))
        composeTestRule.onNodeWithTag(eventVenueTag).assert(hasText(expectedVenue))
    }
}