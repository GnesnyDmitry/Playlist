package com.example.playlistmaker.search.ui

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

data class SearchScreenUiState(
    val isPlaceholderVisible: Boolean = false,
    val placeholder: Int = R.drawable.ic_placeholder_nothing_found,
    val isHistoryHeaderVisible: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val isHistoryButtonClearVisible: Boolean = false,
    val placeholderText: String = "Ничего не нашлось"
    )
