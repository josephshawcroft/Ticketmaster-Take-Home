package com.shawcroftstudios.ticketmastertakehome.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shawcroftstudios.ticketmastertakehome.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier, onQueryChange: (String) -> Unit = { }) {

    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(12.dp),
            ) {
                TextField(
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search_icon)
                        )
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.search_for_event), color = LightGray, fontSize = 12.sp)
                    },
                    singleLine = true,
                    value = query,
                    onValueChange = {
                        query = it
                        onQueryChange(it)
                    },
                    modifier = Modifier
                        .testTag(SEARCH_BAR_TEXT_FIELD_TAG)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    ),
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Icon(Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_text),
                                modifier = Modifier
                                    .testTag(SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG)
                                    .clickable {
                                        query = ""
                                        onQueryChange("")
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }
                            )
                        }
                    }
                )
            }
        }
    }
}

const val SEARCH_BAR_TEXT_FIELD_TAG = "SEARCH_BAR_TEXT_FIELD_TAG"
const val SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG = "SEARCH_BAR_TEXT_FIELD_TRAILING_ICON_TAG"

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}
