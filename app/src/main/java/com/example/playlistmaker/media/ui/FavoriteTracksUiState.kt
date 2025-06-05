package com.example.playlistmaker.media.ui

import androidx.compose.runtime.Immutable
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

@Immutable
data class FavoriteTracksUiState(
    val tracks: List<Track> = emptyList(),
    val isPlaceholderVisible: Boolean = false,
    val placeholder: Int = R.drawable.ic_placeholder_nothing_found,
    val placeholderText: String = ""
)
