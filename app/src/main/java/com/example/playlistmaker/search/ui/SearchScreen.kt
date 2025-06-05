package com.example.playlistmaker.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.theme.CustomTheme
import com.example.playlistmaker.compose.Placeholder
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.TrackListItem
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.model.SearchViewState


@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
) {
    val state by viewModel.resultFlow.collectAsState(initial = SearchViewState.Default)
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current

    SearchScreenContent(
        viewModel.searchScreenUiStateMapper.map(state),
        query = query,
        onQueryChange = {
            query = it
            viewModel.onTextChange(it)
        },
        onClickTrack = { viewModel.onClickedTrack(it, context = context) },
        onClickHistoryClear = { viewModel.onClickedBtnClearHistory() },
        onFocusTextField = { viewModel.showHistoryContent() },
        onUnFocusTextField = { viewModel.hideHistory() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreenContent(
    state: SearchScreenUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClickTrack: (Track) -> Unit,
    onClickHistoryClear: () -> Unit,
    onFocusTextField: () -> Unit,
    onUnFocusTextField: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier.background(CustomTheme.colors.background),
        topBar = {
            TopAppBar(
                backgroundColor = CustomTheme.colors.background,
                contentColor = CustomTheme.colors.text,
                elevation = 0.dp,
                title = {
                    Text(
                        style = CustomTheme.typography.medium,
                        text = stringResource(R.string.search)
                    )
                })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CustomTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(CustomTheme.colors.textField),
            )
            {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (focusState.isFocused && query.isEmpty()) {
                                onFocusTextField()
                            } else {
                                onUnFocusTextField()
                            }
                        },
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    maxLines = 1,
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.TextFieldDecorationBox(
                            value = query,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            placeholder = { Text(stringResource(R.string.search)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_clear_text),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clickable { onQueryChange("") }
                                    )
                                }
                            },
                            singleLine = true,
                            enabled = true,
                            isError = false,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            interactionSource = remember { MutableInteractionSource() },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent
                            )
                        )
                    }
                )
            }

            HistoryHeader(state.isHistoryHeaderVisible)

            Placeholder(state.isPlaceholderVisible, state.placeholder, state.placeholderText)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.tracks) { track ->
                        TrackListItem(track, onClickTrack)
                    }
                }
                if (state.isHistoryButtonClearVisible) {
                    Button(
                        onClick = { onClickHistoryClear() },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = CustomTheme.colors.secondBackground,
                            contentColor = CustomTheme.colors.secondText
                        )
                    ) {
                        Text(
                            style = CustomTheme.typography.secondSmall,
                            text = stringResource(R.string.clear_history)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryHeader(isVisible: Boolean) {
    if (isVisible) {
        Text(
            style = CustomTheme.typography.medium,
            color = CustomTheme.colors.text,
            modifier = Modifier.padding(vertical = 20.dp),
            text = stringResource(R.string.history_search)
        )
    }
}
