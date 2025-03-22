package com.example.playlistmaker.player.ui.model

import com.example.playlistmaker.domain.models.Playlist

sealed interface BottomSheetState {
    data class Playlists(val playlists: List<Playlist>): BottomSheetState
    data class AddNewTrackInPlaylist(val playlistName: String): BottomSheetState
    data class TrackExistInPlaylist(val playlistName: String): BottomSheetState
    object Empty: BottomSheetState
}