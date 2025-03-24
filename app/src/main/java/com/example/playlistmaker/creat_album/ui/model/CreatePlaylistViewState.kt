package com.example.playlistmaker.creat_album.ui.model

sealed interface CreatePlaylistViewState {
    data class SavePlaylist(val name: String): CreatePlaylistViewState
    object Default: CreatePlaylistViewState
    object ShowDialog: CreatePlaylistViewState
    object GoBack: CreatePlaylistViewState
}