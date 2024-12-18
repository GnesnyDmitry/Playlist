package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.ui.model.MediaPlayerState

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    override var state = MediaPlayerState.PREPARED

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer.apply {
            reset()
            setOnPreparedListener { state = MediaPlayerState.PREPARED }
            setDataSource(url)
            prepare()
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
        if (state == MediaPlayerState.PAUSED) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }
}