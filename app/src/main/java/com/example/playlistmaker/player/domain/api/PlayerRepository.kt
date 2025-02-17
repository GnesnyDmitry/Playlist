package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.ui.model.MediaPlayerState

interface PlayerRepository {

    var state: MediaPlayerState

    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun setStopListener(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
}