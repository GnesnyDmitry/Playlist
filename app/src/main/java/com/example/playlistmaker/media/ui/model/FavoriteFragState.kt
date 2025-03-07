package com.example.playlistmaker.media.ui.model

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteFragState {
    data object Empty: FavoriteFragState
    data class Success(val list: List<Track>): FavoriteFragState
}