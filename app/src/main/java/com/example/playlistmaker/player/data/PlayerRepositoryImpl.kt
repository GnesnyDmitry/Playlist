package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.search.data.StateMedialPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    override var state = StateMedialPlayer.STATE_PREPARED

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener { state = StateMedialPlayer.STATE_PREPARED }
            prepareAsync()
        }
    }

    override fun startTrack() {
        state = StateMedialPlayer.STATE_PLAYING
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(action: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            state = StateMedialPlayer.STATE_PREPARED
            action.invoke()
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = StateMedialPlayer.STATE_PAUSE
        }
    }

    override fun stopMediaPlayer() {
        mediaPlayer.release()
    }
}