package com.shawcroftstudios.ticketmastertakehome

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SEARCH_BAR_TEXT_FIELD_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG
import com.shawcroftstudios.ticketmastertakehome.ui.composable.SearchBar
import com.shawcroftstudios.ticketmastertakehome.ui.theme.TicketmasterTakeHomeTheme
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var scenario: ActivityScenario<ComponentActivity>

    private val searchBarTextFieldTag: String = SEARCH_BAR_TEXT_FIELD_TAG
    private val searchBarTrailingIconTag: String = SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG

    private lateinit var placeholderText: String

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(ComponentActivity::class.java)
    }

    @After
    fun teardown() {
        scenario.close()
    }

    @Test
    fun searchBarQueryAndTrailingIconTest() {

        val testQuery = "test query"
        var latestQuery = ""

        setupSearchBar {
            latestQuery = it
        }

        // search bar trailing icon should not exist until text is entered
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(placeholderText))

        // search bar trailing icon should exist after text entry, query should be updated
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).performTextInput(testQuery)
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(testQuery))
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertExists()
        assertEquals(testQuery, latestQuery)

        // search bar trailing icon should again no longer exist after text clearance, query should be empty
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).performTextClearance()
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(placeholderText))
        composeTestRule.onNodeWithTag(searchBarTrailingIconTag).assertDoesNotExist()
        assertEquals("", latestQuery)
    }

    // orientation changes are also considered activity recreation events
    @Test
    fun searchBarActivityRecreationTest() {

        val testQuery = "test query"
        var latestQuery = ""


        setupSearchBar {
            latestQuery = it
        }

        composeTestRule.onNodeWithTag(searchBarTextFieldTag).performTextInput(testQuery)
        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(testQuery))
        assertEquals(testQuery, latestQuery)

        scenario.recreate()

        setupSearchBar {
            latestQuery = it
        }

        composeTestRule.onNodeWithTag(searchBarTextFieldTag).assert(hasText(testQuery))
        assertEquals(testQuery, latestQuery)
    }

    private fun setupSearchBar(onQueryChange: (String) -> Unit) {
        scenario.onActivity { activity ->
            activity.setContent {

                placeholderText = stringResource(id = R.string.search_for_event)

                TicketmasterTakeHomeTheme {
                    SearchBar {
                        onQueryChange(it)
                    }
                }
            }
        }
    }
}