package com.example.playlistmaker.media.ui

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

data class PlaylistScreenUiState(
    val playlists: List<Playlist> = emptyList(),
    val placeholder: Int = R.drawable.ic_placeholder_nothing_found,
    val placeholderText: String = "",
    val isPlaceholderVisible: Boolean = false
)
