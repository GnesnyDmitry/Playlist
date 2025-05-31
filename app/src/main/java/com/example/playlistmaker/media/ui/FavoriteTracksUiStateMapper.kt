package com.example.playlistmaker.media.ui

import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.model.FavoriteFragState

class FavoriteTracksUiStateMapper() {
    fun map(state: FavoriteFragState): FavoriteTracksUiState {
        return when(state) {
            is FavoriteFragState.Empty -> FavoriteTracksUiState(isPlaceholderVisible = true, placeholder = R.drawable.ic_placeholder_nothing_found, placeholderText = "Ваша медиатека пуста")
            is FavoriteFragState.Success -> FavoriteTracksUiState(tracks = state.list)
        }
    }

}