package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.data.StateMedialPlayer

interface PlayerInteractor {
    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun setStopListener(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
    fun getState(): StateMedialPlayer
}