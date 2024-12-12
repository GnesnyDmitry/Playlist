package com.example.playlistmaker.player.ui.model

sealed interface PlayerViewState {
    class PlayBtn(val state: Enum<PlayButtonState>): PlayerViewState
    class TrackTime(val state: Int): PlayerViewState
}