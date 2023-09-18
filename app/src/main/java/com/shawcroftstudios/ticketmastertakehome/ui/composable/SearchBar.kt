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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onQueryChange: (String) -> Unit = { }) {

    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(Modifier.fillMaxWidth()) {
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
                            contentDescription = "Search Icon"
                        )
                    },
                    placeholder = {
                        Text(text = "Search for event", color = LightGray, fontSize = 12.sp)
                    },
                    singleLine = true,
                    value = query,
                    onValueChange = {
                        query = it
                        onQueryChange(it)
                    },
                    modifier = Modifier
                        .testTag("TEXT_FIELD_TEXT_TAG")
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
                                contentDescription = "clear text",
                                modifier = Modifier
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

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}
