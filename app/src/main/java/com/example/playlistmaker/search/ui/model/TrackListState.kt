package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.domain.models.Track

sealed interface TrackListState {

    object Default: TrackListState
    object Loading: TrackListState
    data class SearchContent(val list: List<Track>): TrackListState
    class NoData(val data: List<Track>): TrackListState
    data class HistoryContent(val list: List<Track>): TrackListState
    object Error: TrackListState

}