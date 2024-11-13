package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.model.Track
import java.io.IOException

class PlayButtonManager(
    private val view: PlayerView,
) {

    private var track: Track? = null

    private var mediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null
    private var stateMedialPlayer = StateMedialPlayer.STATE_PREPARED

    fun onClickedBtn() {
        when (stateMedialPlayer) {
            StateMedialPlayer.STATE_PLAYING -> pausePlayer()
            StateMedialPlayer.STATE_PREPARED , StateMedialPlayer.STATE_PAUSE -> startPlayer()
        }
    }

    private fun startTrackTimer() {
        timerRunnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    val currentPosition = mediaPlayer.currentPosition
                    view.updateTrackTimer(currentPosition)
                    handler.postDelayed(this, DELAY_TRACK_TIMER)
                }
            }
        }
        handler.post(timerRunnable!!)
    }

    private fun stopTrackTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
    }

    fun sendTrackForManager(track: Track) {
        this.track = track
        initializePlayer()
    }

    private fun startPlayer() {
        mediaPlayer.start()
        view.changeImageForPlayButton(R.drawable.player_pause)
        stateMedialPlayer = StateMedialPlayer.STATE_PLAYING
        startTrackTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        view.changeImageForPlayButton(R.drawable.player_play)
        stateMedialPlayer = StateMedialPlayer.STATE_PAUSE
    }

    // Инициализация MediaPlayer
    private fun initializePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateMedialPlayer = StateMedialPlayer.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            view.changeImageForPlayButton(R.drawable.player_play)
            stateMedialPlayer = StateMedialPlayer.STATE_PREPARED
        }
    }

    fun onDestroy() {
        stopTrackTimer()
        mediaPlayer.apply {
            stop()
            reset()
            release()
        }
    }

    fun onPause() {
        stopTrackTimer()
        pausePlayer()
    }
    companion object {
        private const val DELAY_TRACK_TIMER = 300L
    }
}