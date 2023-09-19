package com.shawcroftstudios.ticketmastertakehome

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SEARCH_BAR_TEXT_FIELD_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.theme.TicketmasterTakeHomeTheme
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchBarTest() {

        val searchBarTextFieldTag: String = SEARCH_BAR_TEXT_FIELD_TAG
        val searchBarTrailingIconTag: String = SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG

        val testQuery = "test query"
        var latestQuery = ""

        composeTestRule.setContent {
            TicketmasterTakeHomeTheme {
                SearchBar { latestQuery = it }
            }
        }

        // search bar trailing icon should not exist until text is entered
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertDoesNotExist()

        // search bar trailing icon should exist after text entry, query should be updated
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).performTextInput(testQuery)
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(testQuery))
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertExists()
        assertEquals(testQuery, latestQuery)

        // search bar trailing icon should again no longer exist after text clearance, query should be empty
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).performTextClearance()
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(""))
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertDoesNotExist()
        assertEquals("", latestQuery)
    }
}