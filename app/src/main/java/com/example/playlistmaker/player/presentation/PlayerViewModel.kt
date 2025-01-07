package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.DELAY300L

class PlayerViewModel(private val interactor: PlayerInteractor): ViewModel() {

    private val playerViewState = MutableLiveData<PlayerViewState>()

    fun playBtnStateLiveData(): LiveData<PlayerViewState> = playerViewState

    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(url: String) {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        interactor.prepareMediaPlayer(url)
        interactor.onTrackEnd {
            playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
            handler.removeCallbacksAndMessages(null)
        }
    }

    fun stopTrack() {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PAUSE))
        interactor.stopTrack()
        handler.removeCallbacksAndMessages(null)
    }

    fun onClickedBtnPlay() {
        when (interactor.getState()) {
            MediaPlayerState.PREPARED -> onTrackStart()
            MediaPlayerState.PLAYING -> stopTrack()
            MediaPlayerState.PAUSED -> onTrackStart()
            MediaPlayerState.DEFAULT -> playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        }
    }

    private fun onTrackStart() {
        playerViewState.value = PlayerViewState.PlayBtn(PlayButtonState.PLAY)
        interactor.startTrack()
        handler.post(object : Runnable {
            override fun run() {
                playerViewState.postValue(PlayerViewState.TrackTime(interactor.getTime()))
                handler.postDelayed(this, DELAY300L)
            }
        })
    }
}