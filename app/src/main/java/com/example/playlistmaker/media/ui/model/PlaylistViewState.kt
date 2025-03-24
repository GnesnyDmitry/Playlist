package com.example.playlistmaker.media.ui.model

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistViewState {
    data class PlaylistSuccess(val state: List<Playlist>): PlaylistViewState
    object Empty: PlaylistViewState
}