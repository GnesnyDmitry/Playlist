package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.domain.models.Track

sealed interface SearchViewState {
    object Default: SearchViewState
    object Loading: SearchViewState
    data class SearchContent(val list: List<Track>): SearchViewState
    class NoData(val data: List<Track>): SearchViewState
    data class HistoryContent(val list: List<Track>): SearchViewState
    object Error: SearchViewState
}