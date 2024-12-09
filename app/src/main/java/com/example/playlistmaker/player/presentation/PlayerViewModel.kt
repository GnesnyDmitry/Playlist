package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.tools.DELAY300L
import com.example.playlistmaker.tools.SingleLiveEvent

class PlayerViewModel(private val interactor: PlayerInteractor): ViewModel() {

    private val playBtnState = SingleLiveEvent<PlayButtonState>()
    private val timeState = MutableLiveData<Int>()

    fun playBtnStateLiveData(): LiveData<PlayButtonState> = playBtnState
    fun timeStateLiveData(): LiveData<Int> = timeState

    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(url: String) {
        playBtnState.postValue(PlayButtonState.PREPARED)
        interactor.prepareMediaPlayer(url)
        interactor.setStopListener {
            playBtnState.postValue(PlayButtonState.PREPARED)
            handler.removeCallbacksAndMessages(null)
        }
    }

    fun stopTrack() {
        playBtnState.value = PlayButtonState.PAUSE
        interactor.stopTrack()
        handler.removeCallbacksAndMessages(null)
    }

    fun onClickedBtnPlay() {
        when (interactor.getState()) {
            MediaPlayerState.PREPARED -> startTrack()
            MediaPlayerState.PLAYING -> stopTrack()
            MediaPlayerState.PAUSED -> startTrack()
            MediaPlayerState.DEFAULT -> playBtnState.postValue(PlayButtonState.PREPARED)
        }
    }

    private fun startTrack() {
        playBtnState.value = PlayButtonState.PLAY
        interactor.startTrack()
        handler.post(object : Runnable {
            override fun run() {
                timeState.postValue(interactor.getTime())
                handler.postDelayed(this, DELAY300L)
            }
        })
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(interactor = Creator.providePlayerInteractor())
                }
            }
    }
}