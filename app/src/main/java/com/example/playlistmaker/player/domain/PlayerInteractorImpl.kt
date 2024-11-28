package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.data.StateMedialPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val medialPlayer: PlayerRepository) : PlayerInteractor {

    override fun prepareMediaPlayer(url: String) {
        medialPlayer.prepareMediaPlayer(url)
    }

    override fun startTrack() {
        medialPlayer.startTrack()
    }

    override fun getTime(): Int {
        return medialPlayer.getTime()
    }

    override fun setStopListener(action: () -> Unit) {
        medialPlayer.setStopListener(action)
    }

    override fun stopTrack() {
        medialPlayer.stopTrack()
    }

    override fun stopMediaPlayer() {
        medialPlayer.stopMediaPlayer()
    }

    override fun getState(): StateMedialPlayer {
        return medialPlayer.state
    }
}