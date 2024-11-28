package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.StateMedialPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerRouter
import com.example.playlistmaker.tools.DELAY300L

class PlayerPresenter(
    private val view: PlayerView,
    private val router: PlayerRouter,
    private val interactor: PlayerInteractor,
) {
    private val track = view.getTrack()
    private val handler = Handler(Looper.getMainLooper())

    init {
        track.previewUrl?.let { interactor.prepareMediaPlayer(it) }
        interactor.setStopListener {
            view.changeImageForPlayButton(R.drawable.player_play)
            handler.removeCallbacksAndMessages(null)
            view.updateTrackTimer(0)
        }
    }

    fun onClickedBtnPlay() {
        when (interactor.getState()) {
            StateMedialPlayer.STATE_PREPARED -> startTrack()
            StateMedialPlayer.STATE_PLAYING -> stopTrack()
            StateMedialPlayer.STATE_PAUSE -> startTrack()

        }
    }

    private fun startTrack() {
        interactor.startTrack()
        view.changeImageForPlayButton(R.drawable.player_pause)
        handler.post(object : Runnable {
            override fun run() {
                view.updateTrackTimer(interactor.getTime())
                handler.postDelayed(this, DELAY300L)
            }
        })
    }

    fun stopTrack() {
        interactor.stopTrack()
        view.changeImageForPlayButton(R.drawable.player_play)
        handler.removeCallbacksAndMessages(null)
    }

    fun stopMediaPlayer() {
        interactor.stopMediaPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    fun onClickBtnBack() {
        router.goBack()
    }
}