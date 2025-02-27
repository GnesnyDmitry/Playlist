package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.DELAY300L
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val interactor: PlayerInteractor) : ViewModel() {

    private val playerViewState = MutableLiveData<PlayerViewState>()

    fun playBtnStateLiveData(): LiveData<PlayerViewState> = playerViewState

    private var timerJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(url: String) {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        interactor.prepareMediaPlayer(url)
        interactor.onTrackEnd {
            playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        }
    }

    fun stopTrack() {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PAUSE))
        interactor.stopTrack()
        timerJob?.cancel()
    }

    fun onClickedBtnPlay() {
        when (interactor.getState()) {
            MediaPlayerState.PREPARED -> onTrackStart()
            MediaPlayerState.PLAYING -> stopTrack()
            MediaPlayerState.PAUSED -> onTrackStart()
            MediaPlayerState.DEFAULT -> playerViewState.postValue(
                PlayerViewState.PlayBtn(
                    PlayButtonState.PREPARED
                )
            )
        }
    }

    private fun onTrackStart() {
        playerViewState.value = PlayerViewState.PlayBtn(PlayButtonState.PLAY)
        interactor.startTrack()
        timerJob = viewModelScope.launch {
            while (interactor.getState() == MediaPlayerState.PLAYING) {
                delay(DELAY300L)
                playerViewState.postValue(PlayerViewState.TrackTime(interactor.getTime()))
            }
        }
    }
}