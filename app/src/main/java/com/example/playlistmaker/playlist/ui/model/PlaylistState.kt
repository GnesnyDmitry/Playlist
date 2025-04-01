package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistState {
    data object Default: PlaylistState
    data class SuccessBottomSheet(val playlist: Playlist, val tracks: List<Track>, val totalTime: Int): PlaylistState
    data class EmptyBottomSheet(val playlist: Playlist): PlaylistState
    data object EmptyShare: PlaylistState
    data class Share(val playlist: Playlist, val tracks: List<Track>): PlaylistState
    data class Dots(val playlist: Playlist): PlaylistState
//    data class PlaylistTotalTime(val totalTime: Int): PlaylistState
}