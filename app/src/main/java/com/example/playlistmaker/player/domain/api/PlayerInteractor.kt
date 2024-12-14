package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.ui.model.MediaPlayerState

interface PlayerInteractor {
    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun onTrackEnd(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
    fun getState(): MediaPlayerState
}