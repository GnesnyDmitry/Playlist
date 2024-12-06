package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.data.StateMedialPlayer

interface PlayerRepository {

    var state: StateMedialPlayer

    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun setStopListener(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
}