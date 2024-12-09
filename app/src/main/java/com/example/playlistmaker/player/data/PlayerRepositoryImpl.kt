package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.ui.model.MediaPlayerState

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    override var state = MediaPlayerState.PREPARED

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener { state = MediaPlayerState.PREPARED }
            prepareAsync()
        }
    }

    override fun startTrack() {
        state = MediaPlayerState.PLAYING
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(action: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            state = MediaPlayerState.PREPARED
            action.invoke()
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = MediaPlayerState.PAUSED
        }
    }

    override fun stopMediaPlayer() {
        mediaPlayer.release()
    }
}