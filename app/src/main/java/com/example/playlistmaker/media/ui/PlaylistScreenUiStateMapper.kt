package com.example.playlistmaker.media.ui

import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.model.PlaylistViewState

class PlaylistScreenUiStateMapper {
    fun map(state: PlaylistViewState): PlaylistScreenUiState {
        return when (state) {
            is PlaylistViewState.Empty -> PlaylistScreenUiState(placeholder = R.drawable.ic_placeholder_nothing_found, placeholderText = "Вы не создали ни одного плейлиста", isPlaceholderVisible = true)
            is PlaylistViewState.PlaylistSuccess -> PlaylistScreenUiState(playlists = state.playlists)
        }
    }
}