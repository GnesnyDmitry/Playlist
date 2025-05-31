package com.example.playlistmaker.search.ui

import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.model.SearchViewState

class SearchScreenUiStateMapper() {

    fun map(state: SearchViewState): SearchScreenUiState {
        return when (state) {
            is SearchViewState.Default -> SearchScreenUiState(tracks = emptyList())
            is SearchViewState.Error -> SearchScreenUiState(isPlaceholderVisible = true, placeholder = R.drawable.ic_placeholder_no_connection, placeholderText = "Проблемы с сетью")
            is SearchViewState.HistoryContent -> SearchScreenUiState(isHistoryHeaderVisible = true, tracks = state.list, isHistoryButtonClearVisible = true)
            is SearchViewState.Loading -> SearchScreenUiState()
            is SearchViewState.NoData -> SearchScreenUiState(isPlaceholderVisible = true, placeholder = R.drawable.ic_placeholder_nothing_found, placeholderText = "Ничего не нашлось")
            is SearchViewState.SearchContent -> SearchScreenUiState(tracks = state.list)
        }
    }
}