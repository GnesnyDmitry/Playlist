package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    data object Default: PlaylistState
    data class SuccessBottomSheet(val playlist: Playlist): PlaylistState
    data class EmptyBottomSheet(val playlist: Playlist): PlaylistState
    data object EmptyShare: PlaylistState
    data class Share(val playlist: Playlist): PlaylistState
    data class Dots(val playlist: Playlist): PlaylistState
}